package com.zzz.feature_trip.create.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import com.zzz.feature_trip.create.presentation.states.SessionData
import com.zzz.feature_trip.create.presentation.states.TripState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val todoSource: TodoSource
) : ViewModel() {

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

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private var daysFlowJob: Job? = null


    init {
        Log.d("createVm" , "CreateViewModel init")

        createTripSession(
            onDone = {
                getTripDaysFlow()
            }
        )
    }

    fun onAction(action: CreateAction) {
        when (action) {
            //=========== DAY ===========
            is CreateAction.DayActions -> {

                when (action) {
                    CreateAction.DayActions.CreateDaySession -> {
                        createDaySession()
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
                    //save day
                    CreateAction.DayActions.OnSaveDay -> {
                        onAddDay()
                    }

                    CreateAction.DayActions.OnUpdateDay -> {
                        updateDay()
                    }
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
                        onDocUpload(action.docUri)
                    }
                    //title
                    is CreateAction.TripActions.OnTripTitleChange -> {
                        onTripTitleChange(action.title)
                    }
                }
            }

            CreateAction.OnDiscardTripCreation -> {
                discardTripCreation()
            }

            CreateAction.OnSave -> {
                saveTrip()
            }
        }
    }


    //=========== DAY ===========
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

    private fun onAddDay() {
        viewModelScope.launch {
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


//                _dayState.value.todoLocations.onEach { todoLoc ->
//                    launch {
//                        todoSource.addTodo(todoLoc.copy(dayId = dayId))
//                    }
//                }

                sessionData = sessionData.copy(
                    dayIds = sessionData.dayIds + sessionData.dayId
                )
            }

            resetDayState()
        }
    }
    private fun deleteDayById(dayId : Long){
        viewModelScope.launch {
            daySource.deleteDayById(dayId)
        }
    }

    /**
     * Updates title of the day
     */
    private fun updateDay() {
        viewModelScope.launch {
            //update day
            daySource.updateDayById(sessionData.dayId , _dayState.value.dayTitle)

        }
    }

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

            _dayState.update {
                it.copy(
                    dayTitle = day.day.locationName ,
                    image = day.day.image ,
                    todoLocations = day.todosAndLocations ,
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
            Log.d("CreateVM" , "discardDayCreation : Discard day ${sessionData.dayId}")
            daySource.deleteDayById(sessionData.dayId)
            resetDayState()
        }
    }

    private fun resetDayState() {
        viewModelScope.launch {
            delay(300)
            _dayState.update {
                DayState()
            }
            sessionData = sessionData.copy(dayId = 0)
        }
    }


    //=========== TRIP ===========
    private fun onTripTitleChange(title: String) {
        viewModelScope.launch {
            _tripState.update {
                it.copy(tripTitle = title)
            }
        }
    }

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

    private fun onDocUpload(docUri: Uri) {
        viewModelScope.launch {
            _tripState.update {
                it.copy(
                    uploadedDocs = it.uploadedDocs + docUri
                )
            }
        }
    }

    private fun discardTripCreation() {
        viewModelScope.launch {
            delay(400)
            tripSource.deleteTripById(sessionData.tripId)
            resetTripState()
        }
    }


    //========= SAVE =========
    private fun saveTrip() {
        viewModelScope.launch {
            _tripState.update {
                it.copy(saving = true)
            }
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

            //save day
            withContext(Dispatchers.IO) {
                val trip = Trip(
                    id = sessionData.tripId ,
                    tripName = _tripState.value.tripTitle.trim() ,
                    startDate = _tripState.value.startDate!! ,
                    endDate = _tripState.value.endDate!! ,
                    documents = _tripState.value.uploadedDocs
                )
                val tripId = tripSource.updateTrip(trip)
                Log.d("CreateVM" , "saveTrip: Trip saved, TripId - $tripId")


            }
            Log.d("CreateVM" , "saveTrip: All entities SAVED!")
            _tripState.update {
                it.copy(saving = false)
            }
            _events.send(UIEvents.Success)
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
        val day = _dayState.value
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
        viewModelScope.launch {
            val tempTrip = Trip(
                tripName = "null" ,
                startDate = 0L ,
                endDate = 0L
            )
            val id = tripSource.addTrip(tempTrip)
            sessionData = sessionData.copy(tripId = id)
            Log.d("CreateVM" , "createTripSession: Session id $id")
            onDone()
        }

    }

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

    //DB GET REQs
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
                .collect { days ->
                    Log.d("CreateVM" , "getTripDaysFlow: List size is $days")
                    _days.update {
                        days
                    }
                }
        }
    }

    private fun getDayTodosFlow() {
        daysFlowJob = viewModelScope.launch {
            todoSource.getTodosByDayId(sessionData.dayId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("CreateVM" , "getDayTodosFlow: Error")

                    if (it is CancellationException) {
                        throw it
                    } else {
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

    override fun onCleared() {
        super.onCleared()
        Log.d("createVm" , "Clearing CreateViewModel")
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