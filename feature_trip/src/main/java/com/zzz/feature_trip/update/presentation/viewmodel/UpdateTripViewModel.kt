package com.zzz.feature_trip.update.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.source.UserDocSource
import com.zzz.feature_trip.create.presentation.states.CreateAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class UpdateTripViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val todoSource: TodoSource ,
    private val docSource : UserDocSource
) :ViewModel(){

    private var loggingEnabled  : Boolean = true
    private var sessionOngoing  : Boolean = false

    private var dayId : Long = -1L

    private val _state = MutableStateFlow(UpdateTripState())
    internal val state = _state.asStateFlow()

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private var collectTodosJob: Job? = null

    init {
        log {
            "VM init..."
        }
    }

    fun onAction(action: CreateAction) {
        when (action) {
            //=========== DAY ===========
            is CreateAction.DayActions -> {

                when (action) {
                    CreateAction.DayActions.CreateDaySession -> {
                        createDaySession()
                    }
                    CreateAction.DayActions.ClearDayState->{
                        resetDayState()
                    }

                    //title
                    is CreateAction.DayActions.OnDayTitleChange -> {
                        onDayTitleChange(action.title)
                    }
                    //image
                    is CreateAction.DayActions.OnPickImage -> {
                        onPickImage(action.imageUri)
                    }
                    //add todos
                    is CreateAction.DayActions.OnAddTodoLocation -> {
                        onAddTodo(action.title , action.isTodo)
                    }
                    //delete todos
                    is CreateAction.DayActions.OnDeleteTodoLocation -> {
                        deleteTodoByDayId(action.id)
                    }
                    //discard dialog
                    is CreateAction.DayActions.OnDialogVisibilityChange -> {
                        onDialogVisibilityChange(action.visible)
                    }

                    is CreateAction.DayActions.OnDeleteDay -> {
                        deleteDayById(action.id)
                    }
                    //fetch
                    is CreateAction.DayActions.FetchDayById -> {
                        fetchDayById(action.id)
                    }
                    //discard
                    CreateAction.DayActions.OnDiscardCreation -> {
                        discardDayCreation()
                    }

                    CreateAction.DayActions.OnUpdateDay -> {
                        updateDay()
                    }
                    else->Unit
                }
            }

            //=========== TRIP ===========
            is CreateAction.TripActions -> {
                when (action) {

                    // date
                    is CreateAction.TripActions.OnDateSelect -> {
                        onDateSelect(action.start , action.end)
                    }
                    //doc
                    is CreateAction.TripActions.OnDocumentUpload -> {
                        uploadDocument(action.docUri,action.docName)
                    }
                    is CreateAction.TripActions.OnDocumentUpdate->{
                        updateDocument(action.docId,action.newName)
                    }
                    is CreateAction.TripActions.DeleteDocument->{
                        deleteDocument(action.docId)
                    }
                    //title
                    is CreateAction.TripActions.OnTripTitleChange -> {
                        onTripTitleChange(action.title)
                    }

                    is CreateAction.TripActions.FetchTripData->{
                        fetchTripData(action.tripId)
                    }
                    else->Unit

                }
            }


            CreateAction.OnSave -> {
                saveTrip()
            }
            else->Unit

        }
    }

    private fun fetchTripData(tripId : Long){
        if(sessionOngoing){
            return
        }
        viewModelScope.launch {
            sessionOngoing = true
            val trip = tripSource.getTripById(tripId)
            _state.update {
                it.copy(
                    tripId = tripId,
                    tripTitle = trip.tripName,
                    startDate =trip.startDate ,
                    endDate = trip.endDate
                )
            }
            getTripDaysFlow()
            getUserDocsFlow()
        }
    }

    //=========== DAY ===========
    private fun onDayTitleChange(title: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(dayTitle = title)
            }
        }
    }

    //img
    private fun onPickImage(uri: Uri) {
        viewModelScope.launch {
            _state.update {
                it.copy(image = uri)
            }
        }
    }

    private fun onAddTodo(title: String , isTodo: Boolean) {
        if (dayId==-1L) {
            return
        }
        viewModelScope.launch {
            log {
                "onAddTodo : Adding todo $title..."
            }

            val todoLoc = TodoLocation(dayId = dayId , title = title , isTodo = isTodo)
            todoSource.addTodo(todoLoc)
            _state.update {
                it.copy(addTodoDialogVisible = false)
            }
        }
    }

    /**
     * Delete TODOs references by the Day id
     */
    private fun deleteTodoByDayId(id: Long) {
        viewModelScope.launch {
            todoSource.deleteTodo(id)
        }
    }


    private fun validateDayInput(
        onTitleInvalid : ()->Unit
    ) : Boolean{
        if(_state.value.dayTitle.isBlank()){
            onTitleInvalid()
            return false
        }
        return true
    }

    /**
     * Delete Day entity by id
     */
    private fun deleteDayById(dayId : Long){
        viewModelScope.launch {
            daySource.deleteDayById(dayId)
        }
    }
    /**
     * Updates title of the day
     */
    private fun updateDay() {
        if(dayId==-1L){
            return
        }
        val tripId = _state.value.tripId ?: return
        viewModelScope.launch {
            val day = Day(
                id= dayId,
                locationName = _state.value.dayTitle,
                image = _state.value.image,
                tripId = tripId
            )

            daySource.updateDay(day)
            resetDayState()
        }
    }

    /**
     * Add TODOs dialog
     */
    private fun onDialogVisibilityChange(visible: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    addTodoDialogVisible = visible
                )
            }
        }
    }

    /**
     * Fetch Day entry by id
     */
    private fun fetchDayById(id: Long) {
        viewModelScope.launch {
            val day = daySource.getDayById(id)
            dayId = id

            //start fetching todos
            getDayTodosFlow()

            _state.update {
                it.copy(
                    dayTitle = day.locationName ,
                    image = day.image ,
                )
            }
        }
    }



    private fun resetDayState() {
        viewModelScope.launch {
            delay(300)
            collectTodosJob?.let {
                log {
                    "resetDayState : Cancelling TODO flows job"
                }
                collectTodosJob?.cancel()
            }
            _state.update {
                it.copy(
                    dayTitle = "",
                    image = null
                )
            }
            dayId = -1

        }
    }
    /**
     * Deletes the session Day entry in the table & gets rid of all the added TODOs as well
     */
    private fun discardDayCreation() {
        if(dayId == -1L){
            return
        }
        viewModelScope.launch {
            log {
                "discardDayCreation : Discard day $dayId"
            }
            daySource.deleteDayById(dayId)
            resetDayState()
        }
    }

    //=========== TRIP ===========
    private fun onTripTitleChange(title: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(tripTitle = title)
            }
        }
    }

    //TODO - Add constraints
    private fun onDateSelect(start: Long , end: Long) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    startDate = start ,
                    endDate = end
                )
            }
        }
    }

    private fun uploadDocument(docUri: Uri , docName : String) {
        val tripId= _state.value.tripId ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val doc = UserDocument(
                    docName = docName,
                    uri= docUri,
                    tripId = tripId
                )
                val id = docSource.addDocument(doc)
                log{
                    "uploadDocument: Added doc $docName $id"
                }

            }

        }
    }
    private fun deleteDocument(docId : Long){
        viewModelScope.launch {
            docSource.deleteDocumentById(docId)
        }
    }
    private fun updateDocument(docId : Long , newName : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                docSource.updateDocumentById(docId,newName)
            }
        }
    }



    //========= SAVE =========
    private fun saveTrip() {
        viewModelScope.launch {
            val isValid = validateUserInput(
                tripNameEmpty = {
                    _events.trySend(UIEvents.Error("Please enter a trip name"))
                } ,
                tripDatesNull = {
                    _events.trySend(UIEvents.Error("Please select valid start and end dates"))
                }
            )
            val tripId = _state.value.tripId ?: return@launch

            if (!isValid) {
                log {
                    "saveTrip: Invalid input, returning"
                }
                return@launch
            }
            log {
                "saving..."
            }
            _state.update {
                it.copy(saving = true)
            }

            withContext(Dispatchers.IO) {
                val trip = Trip(
                    id = tripId ,
                    tripName = _state.value.tripTitle.trim() ,
                    startDate = _state.value.startDate!! ,
                    endDate = _state.value.endDate!! ,
                )
                tripSource.updateTrip(trip)
                log {
                    "Saved, id $tripId"
                }

            }
            withContext(Dispatchers.Main.immediate){
                delay(1000L)
                _state.update {
                    it.copy(saving = false)
                }
                _events.send(UIEvents.Success)
            }
            resetTripState()
        }
    }

    /**
     * Validates trip title & the dates
     */
    private fun validateUserInput(
        tripNameEmpty: () -> Unit ,
        tripDatesNull: () -> Unit ,
    ): Boolean {
        val trip = _state.value
        //trip name
        if (trip.tripTitle.isBlank()) {
            tripNameEmpty()
            return false
        }
        if (trip.startDate == null || trip.endDate == null) {
            tripDatesNull()
            return false
        }
        return true

    }

    /**
     * Resets trip state, day state & session data
     */
    private fun resetTripState() {
        viewModelScope.launch {
            _state.update { UpdateTripState() }
        }
    }


    /**
     * This function creates an entry for Day into the DB for session management
     * @return - Id of the entry, can be used to clear data if user cancels creation
     */
    private fun createDaySession() {
        val tripId = _state.value.tripId ?: return
        viewModelScope.launch {
            val tempDay = Day(
                locationName = "" ,
                tripId = tripId
            )
            val id = daySource.addDay(tempDay)
            dayId =  id
            log {
                "createTripSession: Session Day id $id"
            }

            //start listening to todos
            getDayTodosFlow()
        }
    }

    //DB GET REQs
    //Days flow
    private fun getTripDaysFlow() {
        viewModelScope.launch {
            if (_state.value.tripId == null) {
                log {
                    "getTripDaysFlow: Invalid session ID, returning..."
                }
                return@launch
            }
            daySource.getDaysByTripId(_state.value.tripId!!)
                .flowOn(Dispatchers.IO)
                .catch {
                    log {
                        "getTripDaysFlow: Error"
                    }
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    if(it != null && it is CancellationException){
                        throw it
                    }
                    log{
                        "getTripDaysFlow: Flow completed"
                    }

                }
                .collect { days ->
                    _state.update {
                        it.copy(
                            days = days
                        )
                    }
                }
        }
    }

    //TODOs flow
    private fun getDayTodosFlow() {
        if(dayId == -1L){
            return
        }
        collectTodosJob = viewModelScope.launch {
            todoSource.getTodosByDayId(dayId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("CreateVM" , "getDayTodosFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect { todoLocs ->
                    _state.update {
                        it.copy(
                            todos = todoLocs
                        )
                    }
                }
        }
    }

    //Docs flow
    private fun getUserDocsFlow(){
        val tripId = _state.value.tripId ?: return
        viewModelScope.launch {
            docSource.getUserDocumentsByTripId(tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    log {
                        "User docs flow error"
                    }
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect{newDocs->
                    _state.update {
                        it.copy(
                            userDocs = newDocs
                        )
                    }

                }
        }
    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("updateTripVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        log {
            "Clearing vm..."
        }
    }


}