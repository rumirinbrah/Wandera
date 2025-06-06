package com.zzz.feature_trip.create.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import com.zzz.feature_trip.create.presentation.states.TripState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log

class CreateViewModel(
    private val tripSource : TripSource
) : ViewModel() {

    private val _tripState = MutableStateFlow(TripState())
    val tripState = _tripState.asStateFlow()

    private val _dayState = MutableStateFlow(DayState())
    val dayState = _dayState.asStateFlow()

    fun onAction(action: CreateAction){
        when(action){

            is CreateAction.OnDayTitleChange -> {
                onDayTitleChange(action.title)
            }
            is CreateAction.OnAddTodoLocation -> {
                onAddTodo(action.todo)
            }
            CreateAction.OnSaveDay ->{

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

    private fun onAddTodo(todo: TodoLocation) {
        viewModelScope.launch {
            _dayState.update {
                it.copy(
                    todoLocations = it.todoLocations + todo
                )
            }
        }
    }

    private fun onAddDay(day: Day) {
        viewModelScope.launch {
            _tripState.update {
                it.copy(
                    days = it.days + day
                )
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