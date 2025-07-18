package com.zzz.feature_trip.create.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.note.model.ChecklistEntity
import com.zzz.data.note.model.ExpenseNote
import com.zzz.data.note.source.ChecklistSource
import com.zzz.data.note.source.ExpenseNoteSource
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.source.UserDocSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val docSource: UserDocSource ,
    private val expenseNoteSource: ExpenseNoteSource ,
    private val checklistSource: ChecklistSource ,
) : ViewModel() {

    private val loggingEnabled = true

    private var sessionData = SessionData()

    private val _tripState = MutableStateFlow(TripState())
    internal val tripState = _tripState.asStateFlow()

    private val _dayState = MutableStateFlow(DayState())
    internal val dayState = _dayState.asStateFlow()

    private val _days = MutableStateFlow<List<Day>>(emptyList())
    val days = _days.stateIn(
        viewModelScope ,
        SharingStarted.WhileSubscribed(5000L) ,
        _days.value
    )

    private val _todos = MutableStateFlow<List<TodoLocation>>(emptyList())
    val todos = _todos.stateIn(
        viewModelScope ,
        SharingStarted.WhileSubscribed(5000L) ,
        _todos.value
    )

    private val _docs = MutableStateFlow<List<UserDocument>>(emptyList())
    val docs = _docs.stateIn(
        viewModelScope ,
        SharingStarted.WhileSubscribed(5000L) ,
        _docs.value
    )


    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private var collectTodosJob: Job? = null
    private var collectDocsJob: Job? = null
    private var collectChecklistJob: Job? = null


    init {
        log {
            "init..."
        }

        /*
        createTripSession(
            onDone = {
                getTripDaysFlow()
                getUserDocsFlow()
                getTripChecklistFlow()
            }
        )

         */

    }

    fun onAction(action: CreateAction) {
        when (action) {

            //=========== TRIP ===========
            is CreateAction.TripActions -> {
                when (action) {

                    //title
                    is CreateAction.TripActions.OnTripTitleChange -> {
                        onTripTitleChange(action.title)
                    }
                    // date
                    is CreateAction.TripActions.OnDateSelect -> {
                        onDateSelect(action.start , action.end)
                    }
                    //doc
                    is CreateAction.TripActions.OnDocumentUpload -> {
                        uploadDocument(action.docUri , action.docName)
                    }

                    is CreateAction.TripActions.OnDocumentUpdate -> {
                        updateDocument(action.docId , action.newName)
                    }

                    is CreateAction.TripActions.DeleteDocument -> {
                        deleteDocument(action.docId)
                    }

                    //checklist
                    is CreateAction.TripActions.ShowAddChecklistDialog ->{
                        changeChecklistDialogVisibility(action.visible)
                    }
                    is CreateAction.TripActions.AddChecklistEntity -> {
                        addChecklistEntity(action.title)
                    }

                    is CreateAction.TripActions.CheckChecklistEntity -> {
                        checkChecklistEntity(action.itemId , action.checked)
                    }

                    is CreateAction.TripActions.DeleteChecklistEntity -> {
                        deleteChecklistEntity(action.itemId)
                    }

                    //create session
                    CreateAction.TripActions.CreateSession -> {
                        createTripSession {
                            getTripDaysFlow()
                            getUserDocsFlow()
                            getTripChecklistFlow()
                        }
                    }
                    is CreateAction.TripActions.FetchTripData ->{
                        fetchTripData(action.tripId)
                    }

                }
            }

            is CreateAction.DayActions.OnDeleteDay->{
                deleteDayById(action.id)
            }

            CreateAction.OnDiscardTripCreation -> {
                discardTripCreation()
            }

            CreateAction.OnSave -> {
                saveTrip()
            }
            else->{}

        }
    }

    /*
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
     */

    //=========== DAY ===========
    /**
     * Delete Day entity by id
     */
    private fun deleteDayById(dayId: Long) {
        viewModelScope.launch {
            daySource.deleteDayById(dayId)
        }
    }
    /*
    private fun onDayTitleChange(title: String) {
        viewModelScope.launch {
            _dayState.update {
                it.copy(dayTitle = title)
            }
        }
    }

    //img
    private fun onPickImage(uri: Uri) {
        viewModelScope.launch {
            _dayState.update {
                it.copy(image = uri)
            }
        }
    }

    private fun onAddTodo(title: String , isTodo: Boolean) {
        viewModelScope.launch {
            Log.d("CreateVM" , "onAddTodo : Adding todo $title...")

            val todoLoc = TodoLocation(dayId = sessionData.dayId , title = title , isTodo = isTodo)
            todoSource.addTodo(todoLoc)
            _dayState.update {
                it.copy(dialogVisible = false)
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

    private fun saveDay() {
        viewModelScope.launch {
            val isValid = validateDayInput(
                onTitleInvalid = {
                    _events.trySend(UIEvents.Error("Please enter a valid title"))
                }
            )
            if (!isValid) return@launch

            withContext(Dispatchers.IO) {
                val day = Day(
                    id = sessionData.dayId ,
                    locationName = _dayState.value.dayTitle ,
                    image = _dayState.value.image ,
                    isDone = false ,
                    tripId = sessionData.tripId
                )

                val dayId = daySource.updateDay(day)

                Log.d("CreateVM" , "onAddDay : Added day ${day.locationName}")
                Log.d("CreateVM" , "onAddDay : Day id $dayId")

                sessionData = sessionData.copy(
                    dayIds = sessionData.dayIds + sessionData.dayId
                )
            }

            resetDayState()
        }
    }

    private fun validateDayInput(
        onTitleInvalid: () -> Unit
    ): Boolean {
        if (_dayState.value.dayTitle.isBlank()) {
            onTitleInvalid()
            return false
        }
        return true
    }



    /**
     * Updates title of the day
     */
    private fun updateDay() {
        viewModelScope.launch {
            val day = Day(
                id = sessionData.dayId ,
                locationName = _dayState.value.dayTitle ,
                image = _dayState.value.image ,
                tripId = sessionData.tripId
            )
            //update day
            //daySource.updateDayById(sessionData.dayId , _dayState.value.dayTitle)
            daySource.updateDay(day)
            resetDayState()
        }
    }

    /**
     * Add TODOs dialog
     */
    private fun onDialogVisibilityChange(visible: Boolean) {
        viewModelScope.launch {
            _dayState.update {
                it.copy(
                    dialogVisible = visible
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
            sessionData = sessionData.copy(dayId = id)

            //start fetching todos
            getDayTodosFlow()

            _dayState.update {
                it.copy(
                    dayTitle = day.locationName ,
                    image = day.image ,
                    isUpdating = true
                )
            }
        }
    }

    /**
     * Deletes the session Day entry in the table & gets rid of all the added TODOs as well
     */
    private fun discardDayCreation() {
        viewModelScope.launch {
            log {
                "discardDayCreation : Discard day ${sessionData.dayId}"
            }
            daySource.deleteDayById(sessionData.dayId)
            resetDayState()
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
            _dayState.update {
                DayState()
            }
            sessionData = sessionData.copy(dayId = 0)
        }
    }

     */


    //=========== TRIP ===========
    private fun onTripTitleChange(title: String) {
        viewModelScope.launch {
            _tripState.update {
                it.copy(tripTitle = title)
            }
        }
    }

    //TODO - Add constraints
    private fun onDateSelect(start: Long , end: Long) {
        viewModelScope.launch {
            _tripState.update {
                it.copy(
                    startDate = start ,
                    endDate = end
                )
            }
        }
    }

    //-------- DOC --------
    private fun uploadDocument(docUri: Uri , docName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val doc = UserDocument(
                    docName = docName ,
                    uri = docUri ,
                    tripId = sessionData.tripId
                )
                val id = docSource.addDocument(doc)
                Log.d("CreateVM" , "uploadDocument: Added doc $docName $id")

            }

        }
    }

    private fun deleteDocument(docId: Long) {
        viewModelScope.launch {
            docSource.deleteDocumentById(docId)
        }
    }

    private fun updateDocument(docId: Long , newName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                docSource.updateDocumentById(docId , newName)
            }
        }
    }

    //-------- CHECKLIST --------
    private fun changeChecklistDialogVisibility(visible: Boolean){
        viewModelScope.launch {
            _tripState.update {
                it.copy(showAddChecklistDialog = visible)
            }
        }
    }
    private fun addChecklistEntity(title: String) {
        if (title.isBlank()) {
            changeChecklistDialogVisibility(false)
            return
        }
        viewModelScope.launch {
            val item = ChecklistEntity(
                title = title.trim() ,
                tripId = sessionData.tripId
            )
            changeChecklistDialogVisibility(false)
            val id = checklistSource.addItem(item)
            log {
                "Checklist item added, id $id"
            }
        }
    }

    private fun checkChecklistEntity(itemId: Long , checked: Boolean) {
        viewModelScope.launch {
            log {
                "Marking $itemId as $checked"
            }
            checklistSource.checkItem(itemId , checked)
        }
    }

    private fun deleteChecklistEntity(itemId: Long) {
        viewModelScope.launch {
            checklistSource.deleteItem(itemId)
        }
    }

    /**
     * Deletes the session trip resulting in deletion of child table entries as well
     */
    private fun discardTripCreation() {
        viewModelScope.launch {
            delay(400)
            val id = tripSource.deleteTripById(sessionData.tripId)
            Log.d("CreateVM" , "discardTripCreation : Deleted trip $id")

            resetTripState()
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
            if (!isValid) {
                Log.d("CreateVM" , "saveTrip: Invalid input, returning")
                return@launch
            }
            Log.d("CreateVM" , "saveTrip: Input valid, saving...")
            _tripState.update {
                it.copy(saving = true)
            }

            withContext(Dispatchers.IO) {
                val trip = Trip(
                    id = sessionData.tripId ,
                    tripName = _tripState.value.tripTitle.trim() ,
                    startDate = _tripState.value.startDate!! ,
                    endDate = _tripState.value.endDate!! ,
                )
                val tripId = tripSource.updateTrip(trip)

                //add note
                sessionData.tripId.apply {
                    val note = ExpenseNote(
                        tripId = this ,
                    )
                    val noteId = expenseNoteSource.addNote(note)
                    Log.d("CreateVM" , "saveTrip: Expense note saved! ID - $noteId")

                }

                Log.d("CreateVM" , "saveTrip: Trip saved, TripId - $tripId")


            }
            Log.d("CreateVM" , "saveTrip: All entities SAVED!")
            withContext(Dispatchers.Main.immediate) {
                delay(1000L)
                _tripState.update {
                    it.copy(saving = false)
                }
                _events.send(UIEvents.Success)
            }
            //resetDayState()
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
        val trip = _tripState.value
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
            _tripState.update {
                TripState()
            }
            _dayState.update {
                DayState()
            }
            sessionData = SessionData()
        }
    }

    /**
     * This function creates an entry into the DB for session management
     * @return - Id of the entry, can be used to clear data if user cancels creation
     */
    private fun createTripSession(onDone: () -> Unit) {
        if (_tripState.value.sessionOngoing) {
            return
        }
        viewModelScope.launch {
            _tripState.update {
                it.copy(sessionOngoing = true)
            }
            val tempTrip = Trip(
                tripName = "null" ,
                startDate = 0L ,
                endDate = 0L
            )
            val id = tripSource.addTrip(tempTrip)
            _tripState.update {
                it.copy(tripId = id)
            }
            sessionData = sessionData.copy(tripId = id)

            Log.d("CreateVM" , "createTripSession: Session id $id")
            onDone()
        }

    }
    private fun fetchTripData(tripId : Long){
        if(_tripState.value.sessionOngoing){
            return
        }
        viewModelScope.launch {
            log {
                "Fetching trip data for $tripId..."
            }
            _tripState.update {
                it.copy(
                    sessionOngoing = true,
                    isUpdating = true
                )
            }
            sessionData = sessionData.copy(tripId = tripId)


            val trip = tripSource.getTripById(tripId)
            _tripState.update {
                it.copy(
                    tripId = tripId ,
                    tripTitle = trip.tripName ,
                    startDate = trip.startDate ,
                    endDate = trip.endDate
                )
            }
            getTripDaysFlow()
            getUserDocsFlow()
            getTripChecklistFlow()
        }
    }

    /*
    /**
     * This function creates an entry for Day into the DB for session management
     * @return - Id of the entry, can be used to clear data if user cancels creation
     */
    private fun createDaySession() {
        viewModelScope.launch {
            val tempDay = Day(
                locationName = "" ,
                tripId = sessionData.tripId
            )
            val id = daySource.addDay(tempDay)
            sessionData = sessionData.copy(dayId = id)
            Log.d("CreateVM" , "createTripSession: Session Day id $id")
            //start listening to todos
            getDayTodosFlow()
        }
    }

     */

    //DB GET REQs
    //Days flow
    private fun getTripDaysFlow() {
        viewModelScope.launch {
            if (sessionData.tripId == 0L) {
                Log.d("CreateVM" , "getTripDaysFlow: Invalid session ID, returning...")
                return@launch
            }
            daySource.getDaysByTripId(sessionData.tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("CreateVM" , "getTripDaysFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    if (it != null && it is CancellationException) {
                        throw it
                    }
                    Log.d("CreateVM" , "getTripDaysFlow: Flow completed")

                }
                .collect { days ->
                    Log.d("CreateVM" , "getTripDaysFlow: List size is ${days.size}")
                    _days.update {
                        days
                    }
                }
        }
    }

    /*
    //TODOs flow
    private fun getDayTodosFlow() {
        collectTodosJob = viewModelScope.launch {
            todoSource.getTodosByDayId(sessionData.dayId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("CreateVM" , "getDayTodosFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg ->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect { todoLocs ->
                    _todos.update {
                        todoLocs
                    }
                }
        }
    }

     */

    //Docs flow
    private fun getUserDocsFlow() {
        collectDocsJob = viewModelScope.launch {
            docSource.getUserDocumentsByTripId(sessionData.tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("CreateVM" , "getUserDocsFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg ->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect { newDocs ->
                    _docs.update {
                        newDocs
                    }
                }
        }
    }

    private fun getTripChecklistFlow() {
        collectChecklistJob = viewModelScope.launch {
            log {
                "Starting checklist flow..."
            }
            checklistSource.getChecklistItems(sessionData.tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    log {
                        "getTripChecklistFlow: Error"
                    }
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg ->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect { list ->
                    _tripState.update {
                        it.copy(
                            checklist = list
                        )
                    }
                }
        }
    }

    private fun log(msg: () -> String) {
        if (loggingEnabled) {
            Log.d("createVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("createVm" , "Clearing CreateViewModel")
        collectTodosJob?.let {
            Log.d("CreateVM" , "onCleared : Cancelling TODO flows job")
            collectTodosJob?.cancel()
        }
        collectDocsJob?.let {
            Log.d("CreateVM" , "onCleared : Cancelling DOC flows job")
            collectTodosJob?.cancel()
        }
    }
}
/*
//-----DAY-----
            //title
            is CreateAction.OnDayTitleChange -> {
                onDayTitleChange(action.title)
            }
            //img
            is CreateAction.OnPickImage -> {
                onPickImage(action.imageUri)
            }

            //add
            is CreateAction.OnAddTodoLocation -> {
                onAddTodo(action.title , action.isTodo)
            }
            //delete
            is CreateAction.OnDeleteTodoLocation -> {
                onDeleteTodo(action.todoLocation)
            }

            //dialog
            is CreateAction.OnDialogVisibilityChange -> {
                onDialogVisibilityChange(action.visible)
            }
            //fetch
            is CreateAction.FetchDayById -> {
                fetchDayById(action.id)
            }

            CreateAction.OnSaveDay -> {
                onAddDay()
            }

            CreateAction.OnDiscard -> {
                resetDayState()
            }

            is CreateAction.OnTripTitleChange -> {
                onTripTitleChange(action.title)
            }

            is CreateAction.OnDateSelect -> {
                onDateSelect(action.start , action.end)
            }

            is CreateAction.OnDocumentUpload -> {
                onDocUpload(action.docUri)
            }

            CreateAction.OnSave -> {
                saveTrip()
            }
 */