package com.zzz.feature_trip.create.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * responsible for maintaining session and data related to Day while creating a trip
 */
class DayViewModel(
    private val daySource: DaySource ,
    private val todoSource: TodoSource ,
) : ViewModel() {

    private val loggingEnabled = true

    private val _state = MutableStateFlow(DayState())
    internal val state = _state.asStateFlow()

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()


    private var sessionData: SessionData = SessionData()
    private var collectTodosJob: Job? = null

    init {
        log { "DayVm init..." }
    }

    fun onAction(action: CreateAction.DayActions) {
        when (action) {
            //session
            is CreateAction.DayActions.CreateDaySession -> {
                createDaySession(action.tripId)
            }

            is CreateAction.DayActions.FetchDayById -> {
                fetchDayData(action.id)
            }

            //UI
            is CreateAction.DayActions.OnDayTitleChange -> {
                onDayTitleChange(action.title)
            }

            is CreateAction.DayActions.OnPickImage -> {
                onPickImage(action.imageUri)
            }

            //TODOs
            is CreateAction.DayActions.OnAddTodoLocation -> {
                onAddTodo(
                    action.title ,
                    action.isTodo
                )
            }

            is CreateAction.DayActions.OnDeleteTodoLocation -> {
                deleteTodoByDayId(action.id)
            }
            is CreateAction.DayActions.OnDialogVisibilityChange -> {
                onDialogVisibilityChange(action.visible)
            }

            CreateAction.DayActions.ClearDayState -> TODO()

            CreateAction.DayActions.OnSaveDay -> {
                saveDay()
            }
            CreateAction.DayActions.OnUpdateDay -> {
                updateDay()
            }

            CreateAction.DayActions.OnDiscardCreation -> {
                discardDayCreation()
            }
            is CreateAction.DayActions.OnDeleteDay -> TODO()
        }
    }

    /**
     * This function creates an entry for Day into the DB for session management
     */
    private fun createDaySession(tripId: Long) {
        viewModelScope.launch {
            val tempDay = Day(
                locationName = "" ,
                tripId = tripId
            )
            val id = daySource.addDay(tempDay)
            sessionData = sessionData.copy(
                dayId = id,
                tripId = tripId
            )
            getDayTodosFlow()
        }
    }

    private fun fetchDayData(id: Long) {
        viewModelScope.launch {
            val day = daySource.getDayById(id)
            sessionData = sessionData.copy(
                dayId = id,
                tripId = day.tripId
            )

            //start fetching todos
            getDayTodosFlow()

            _state.update {
                it.copy(
                    dayTitle = day.locationName ,
                    image = day.image ,
                    isUpdating = true
                )
            }
        }
    }

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

    // ----- TODOs -----
    private fun onAddTodo(title: String , isTodo: Boolean) {
        if (sessionData.dayId == 0L) {
            return
        }
        viewModelScope.launch {
            log {
                "onAddTodo : Adding todo $title..."
            }

            val todoLoc = TodoLocation(dayId = sessionData.dayId , title = title.trim() , isTodo = isTodo)
            todoSource.addTodo(todoLoc)
            _state.update {
                it.copy(dialogVisible = false)
            }
        }
    }
    /**
     * Delete TODOs
     */
    private fun deleteTodoByDayId(id: Long) {
        viewModelScope.launch {
            todoSource.deleteTodo(id)
        }
    }
    /**
     * Add TODOs dialog
     */
    private fun onDialogVisibilityChange(visible: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    dialogVisible = visible
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
            _events.send(UIEvents.Success)
            //resetDayState()
        }
    }

    private fun saveDay() {
        if(sessionData.dayId == 0L || sessionData.tripId == 0L){
            _events.trySend(UIEvents.Error("Couldn't save the day, please try again later"))
            return
        }
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                val day = Day(
                    id = sessionData.dayId ,
                    locationName = _state.value.dayTitle ,
                    image = _state.value.image ,
                    isDone = false ,
                    tripId = sessionData.tripId
                )

                val dayId = daySource.updateDay(day)
                _events.send(UIEvents.Success)
                log {
                    "onAddDay : Added day ${day.locationName}; Day id $dayId"
                }

            }

            //resetDayState()
        }
    }
    /**
     * Updates title of the day
     */
    private fun updateDay() {
        viewModelScope.launch {
            val day = Day(
                id = sessionData.dayId ,
                locationName = _state.value.dayTitle ,
                image = _state.value.image ,
                tripId = sessionData.tripId
            )

            daySource.updateDay(day)
            _events.send(UIEvents.Success)

            //resetDayState()
        }
    }

    //TODOs flow
    private fun getDayTodosFlow() {
        collectTodosJob = viewModelScope.launch {
            todoSource.getTodosByDayId(sessionData.dayId)
                .flowOn(Dispatchers.IO)
                .catch {
                    log { "getDayTodosFlow: Error" }
                    if (it is CancellationException) {
                        throw it
                    } else {

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

    private fun log(msg: () -> String) {
        if (loggingEnabled) {
            Log.d("createVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        log {
            "DayVm, clearing VM..."
        }
        collectTodosJob?.let {
            log {
                "Cancelling todos job"
            }
            it.cancel()
        }
    }


}