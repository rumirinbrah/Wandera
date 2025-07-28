package com.zzz.feature_trip.recents.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.source.TripSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecentsViewModel(
    private val tripSource: TripSource
) : ViewModel() {

    private val loggingEnabled= true
    private var sessionOngoing : Boolean = false

    private val _state = MutableStateFlow(RecentTripsState())
    internal val state = _state.asStateFlow()


    private var flowTripsJob : Job? = null

    init {
        log {
            "Init..."
        }
        collectTripsFlow()
    }

    fun onAction(action : RecentTripsAction){
        when(action){

        }
    }

    /**
     * Collects flow of the trips marked as done
     */
    private fun collectTripsFlow(){
        flowTripsJob = viewModelScope.launch {
            tripSource.getFinishedTripWithDays()
                .flowOn(Dispatchers.IO)
                .onCompletion {
                    log {
                        "collectTripsFlow : onCompletion"
                    }
                }
                .catch {
                    if(it is CancellationException){
                        throw it
                    }
                    log {
                        "collectTripsFlow : Error"
                    }
                    it.printStackTrace()
                }
                .collect{newList->
                    _state.update {
                        it.copy(recents = newList)
                    }
                }
        }
    }


    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("recentsVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        flowTripsJob?.cancel()
        log {
            "Clearing vm..."
        }
    }

}