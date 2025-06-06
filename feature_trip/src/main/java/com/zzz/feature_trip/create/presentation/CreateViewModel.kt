package com.zzz.feature_trip.create.presentation

import androidx.lifecycle.ViewModel
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import com.zzz.feature_trip.create.presentation.states.TripState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateViewModel(
    private val tripSource : TripSource
) : ViewModel() {

    private val _tripState = MutableStateFlow(TripState())
    val tripState = _tripState.asStateFlow()

    private val _dayState = MutableStateFlow(DayState())
    val dayState = _dayState.asStateFlow()

    fun onAction(action: CreateAction){
        when(action){

            is CreateAction.OnDayTitleChange -> TODO()
            is CreateAction.OnAddTodoLocation -> TODO()

            is CreateAction.OnTripTitleChange -> TODO()
            is CreateAction.OnDateSelect -> TODO()
            is CreateAction.OnDocumentUpload -> TODO()

            CreateAction.OnSave -> TODO()
        }
    }





}