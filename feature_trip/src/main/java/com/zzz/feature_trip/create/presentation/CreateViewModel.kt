package com.zzz.feature_trip.create.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import com.zzz.feature_trip.create.presentation.states.TripState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateViewModel(
    private val tripSource : TripSource
) : ViewModel() {

    private val _tripState = MutableStateFlow(TripState())
    val tripState = _tripState.asStateFlow()

    private val _dayState = MutableStateFlow(DayState())
    val dayState = _dayState.asStateFlow()

    private var dayId = 0L
    private var dayNo = 1



    fun onAction(action: CreateAction){
        when(action){

            //-----DAY-----
            //title
            is CreateAction.OnDayTitleChange -> {
                onDayTitleChange(action.title)
            }
            //img
            is CreateAction.OnPickImage->{
                onPickImage(action.imageUri)
            }

            //add
            is CreateAction.OnAddTodoLocation -> {
                onAddTodo(action.title,action.isTodo)
            }
            //delete
            is CreateAction.OnDeleteTodoLocation->{
                onDeleteTodo(action.todoLocation)
            }

            //dialog
            is CreateAction.OnDialogVisibilityChange->{
                onDialogVisibilityChange(action.visible)
            }
            //fetch
            is CreateAction.FetchDayById ->{
                fetchDayById(action.id)
            }

            CreateAction.OnSaveDay ->{
                onAddDay()
            }
            CreateAction.OnDiscard ->{
                resetDayState()
            }

            is CreateAction.OnTripTitleChange -> {
                onTripTitleChange(action.title)
            }
            is CreateAction.OnDateSelect -> {
                onDateSelect(action.start,action.end)
            }
            is CreateAction.OnDocumentUpload -> {
                onDocUpload(action.docUri)
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
    private fun onPickImage(uri : Uri){
        viewModelScope.launch {
            _dayState.update {
                it.copy(image = uri)
            }
        }
    }

    private fun onAddTodo(title: String, isTodo : Boolean) {
        viewModelScope.launch {
            //assigning random id for lazy col animations...reassign to 0 while dao ops
            val todo = TodoLocation(
                title = title ,
                isTodo = isTodo
            )
            _dayState.update {
                it.copy(
                    todoLocations = it.todoLocations + todo,
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
                id=dayId,
                dayNo = _tripState.value.days.size+1,
                locationName = _dayState.value.dayTitle,
                isDone = false,
                tripId = 0
            )
            println("Adding day ${day.locationName}")
            val dayWithTodos = DayWithTodos(
                day = day,
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

    private fun onDialogVisibilityChange(visible : Boolean){
        viewModelScope.launch {
            _dayState.update {
                it.copy(
                    dialogVisible = visible
                )
            }
        }
    }
    private fun fetchDayById(id : Long){
        viewModelScope.launch {
            val day = _tripState.value.days.find { it.day.id == id } ?: return@launch

            _dayState.update {
                it.copy(
                    dayNo = day.day.dayNo,
                    dayTitle = day.day.locationName,
                    todoLocations = day.todosAndLocations,
                    uiEnabled = false
                )
            }
        }
    }
    fun getDayNo() : Int{
        return dayNo
    }
    private fun resetDayState(){
        viewModelScope.launch {
            delay(300)
            _dayState.update {
                DayState()
            }
        }
    }


    //=========== TRIP ===========
    private fun onTripTitleChange(title : String){
        viewModelScope.launch {
            _tripState.update {
                it.copy(tripTitle = title)
            }
        }
    }

    private fun onDateSelect(start : Long, end : Long){
        viewModelScope.launch {
            _tripState.update {
                it.copy(
                    startDate = start,
                    endDate = end
                )
            }
        }
    }

    private fun onDocUpload(docUri: Uri){
        viewModelScope.launch {
            _tripState.update {
                it.copy(
                    uploadedDocs = it.uploadedDocs + docUri
                )
            }
        }
    }

    private fun saveTrip(){
        viewModelScope.launch {

        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("viewmodel", "Clearing CreateViewModel")
    }
}