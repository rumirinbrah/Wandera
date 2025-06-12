package com.zzz.feature_trip.overview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TripSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val tripSource: TripSource,
    private val daySource: DaySource
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






}