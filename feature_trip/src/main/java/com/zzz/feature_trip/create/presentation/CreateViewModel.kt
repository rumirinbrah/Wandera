package com.zzz.feature_trip.create.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import com.zzz.feature_trip.create.presentation.states.TripState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val todoSource: TodoSource
) : ViewModel() {

    private val _tripState = MutableStateFlow(TripState())
    val tripState = _tripState.asStateFlow()

    private var tripRoomId = 0

    private val _dayState = MutableStateFlow(DayState())
    val dayState = _dayState.asStateFlow()

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private var dayId = 0L
    private var dayNo = 1


    fun onAction(action: CreateAction) {
        when ( action) {
            //=========== DAY ===========
            is CreateAction.DayActions -> {

                when(action){
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
                        onAddTodo(action.title,action.isTodo)
                    }
                    //delete todos
                    is CreateAction.DayActions.OnDeleteTodoLocation -> {
                        onDeleteTodo(action.todoLocation)
                    }
                    //discard dialog
                    is CreateAction.DayActions.OnDialogVisibilityChange -> {
                        onDialogVisibilityChange(action.visible)
                    }
                    //fetch
                    is CreateAction.DayActions.FetchDayById -> {
                        fetchDayById(action.id)
                    }
                    //discard
                    CreateAction.DayActions.OnDiscard -> {
                        resetDayState()
                    }
                    //save day
                    CreateAction.DayActions.OnSaveDay -> {
                        onAddDay()
                    }
                }
            }

            //=========== TRIP ===========
            is CreateAction.TripActions -> {
                when(action){

                    // date
                    is CreateAction.TripActions.OnDateSelect -> {
                        onDateSelect(action.start,action.end)
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
            //assigning random id for lazy col animations...reassign to 0 while dao ops
            val todo = TodoLocation(
                title = title ,
                isTodo = isTodo
            )
            _dayState.update {
                it.copy(
                    todoLocations = it.todoLocations + todo ,
                    dialogVisible = false
                )
            }
        }
    }

    private fun onDeleteTodo(todoLocation: TodoLocation) {
        viewModelScope.launch {
            _dayState.update {
                it.copy(
                    todoLocations = it.todoLocations - todoLocation
                )
            }
        }
    }

    private fun onAddDay() {
        viewModelScope.launch {
            val day = Day(
                id = dayId ,
                locationName = _dayState.value.dayTitle ,
                image = _dayState.value.image ,
                isDone = false ,
                tripId = 0
            )
            println("Adding day ${day.locationName}")
            val dayWithTodos = DayWithTodos(
                day = day ,
                todosAndLocations = _dayState.value.todoLocations
            )

            _tripState.update {
                it.copy(
                    days = it.days + dayWithTodos
                )
            }
            dayId++
            dayNo++
            resetDayState()
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

    private fun fetchDayById(id: Long) {
        viewModelScope.launch {
            val day = _tripState.value.days.find { it.day.id == id } ?: return@launch

            _dayState.update {
                it.copy(
                    dayTitle = day.day.locationName ,
                    image = day.day.image ,
                    todoLocations = day.todosAndLocations ,
                    uiEnabled = false
                )
            }
        }
    }


    private fun resetDayState() {
        viewModelScope.launch {
            delay(300)
            _dayState.update {
                DayState()
            }
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

    //========= SAVE =========
    private fun saveTrip() {
        //step 1 - Save Trip, get id
        //step 2 - Save Day, get id
        //step 3 - Save Todos by Day id
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
                    tripName = _tripState.value.tripTitle.trim() ,
                    startDate = _tripState.value.startDate!! ,
                    endDate = _tripState.value.endDate!! ,
                    documents = _tripState.value.uploadedDocs
                )
                val tripId = tripSource.addTrip(trip)
                Log.d("CreateVM" , "saveTrip: Trip saved, TripId - $tripId")

                coroutineScope {

                    _tripState.value.days.onEach { dayWithTodos ->
                            launch {

                                val day = Day(
                                    locationName = dayWithTodos.day.locationName.trim() ,
                                    tripId = tripId ,
                                    image = dayWithTodos.day.image
                                )
                                Log.d("CreateVM" , "saveTrip: Saving day, ${day.locationName}")

                                val dayId = daySource.addDay(day)
                                Log.d("CreateVM" , "saveTrip: DAY saved, DayId - $dayId")

                                dayWithTodos.todosAndLocations.onEach { todoLoc ->
                                    val todo = TodoLocation(
                                        title = todoLoc.title.trim() ,
                                        isTodo = todoLoc.isTodo ,
                                        dayId = dayId
                                    )
                                    todoSource.addTodo(todo)
                                }
                                Log.d("CreateVM" , "saveTrip: All TODOS saved!, DayId - $dayId")
                            }

                    }
                }
            }
            Log.d("CreateVM" , "saveTrip: All entities SAVED!")
            _tripState.update {
                it.copy(saving = false)
            }
            resetDayState()
            resetTripState()
        }
    }

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

    private fun resetTripState() {
        viewModelScope.launch {
            _tripState.update {
                TripState()
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.d("viewmodel" , "Clearing CreateViewModel")
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