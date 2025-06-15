package com.zzz.feature_trip.overview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewViewModel(
    private val tripSource: TripSource,
    private val daySource: DaySource,
    private val todoSource: TodoSource
) : ViewModel() {

    private val _overviewState = MutableStateFlow(OverviewState())
    internal val overviewState = _overviewState.asStateFlow()

    init {
        Log.d("overviewVm" , "Init....")
    }

    fun onAction(action: OverviewActions){
        when(action){
            is OverviewActions.FetchTripData -> {
                fetchTripData(action.tripId)
            }
            is OverviewActions.NavigateToEditTrip -> {

            }
            is OverviewActions.UpdateSelectedDay ->{
                updateSelectedDay(action.day)
            }

            OverviewActions.ClearSelectedDay -> {
                clearSelectedDay()
            }
            OverviewActions.ChangeItineraryLayout -> {
                changeItineraryLayout()
            }
        }
    }

    private fun fetchTripData(tripId : Long){
        viewModelScope.launch {
            _overviewState.update {
                it.copy(loading = true)
            }

            try {
                val trip = tripSource.getTripById(tripId)
                val days = daySource.getDaysByTripIdOnce(tripId)
                _overviewState.update {
                    it.copy(
                        trip = trip,
                        days = days,
                        loading = false
                    )
                }

            }catch (e : Exception){
                e.printStackTrace()
                return@launch
            }

        }
    }
    private fun updateSelectedDay(day: Day){
        viewModelScope.launch {
            _overviewState.update {
                it.copy(
                    loading = true
                )
            }
            withContext(Dispatchers.IO){
                val todos = todoSource.getTodosByDayIdOnce(day.id)
                val dayWithTodos = DayWithTodos(
                    day = day,
                    todosAndLocations = todos
                )
                withContext(Dispatchers.Main){
                    _overviewState.update {
                        it.copy(
                            loading = false,
                            selectedDay = dayWithTodos
                        )
                    }
                }
            }


        }
    }
    private fun clearSelectedDay(){
        viewModelScope.launch {
            delay(500)
            _overviewState.update {
                it.copy(selectedDay = null)
            }
        }
    }
    private fun changeItineraryLayout(){
        viewModelScope.launch {
            _overviewState.update {
                it.copy(itineraryPagerLayout = !it.itineraryPagerLayout)
            }
        }
    }






}