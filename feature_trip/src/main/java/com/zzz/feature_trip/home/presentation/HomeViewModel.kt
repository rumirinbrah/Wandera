package com.zzz.feature_trip.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.TripWithDays
import com.zzz.data.trip.source.TripSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel(
    private val tripSource: TripSource
) : ViewModel() {

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private val _tripWithDocs = MutableStateFlow<List<TripWithDays>>(emptyList())
    val tripWithDocs = _tripWithDocs.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _tripWithDocs.value
    )

    private var collectTripsJob : Job? = null

    init {
        getTripsFlow()
    }

    //collect
    private fun getTripsFlow(){
        collectTripsJob = viewModelScope.launch {

            tripSource.getTripsWithUserDocs()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("HomeVM" , "getTripsFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect{trips->
                    Log.d("HomeVM" , "getTripsFlow: New data emitted, list size ${trips.size}")
                    _tripWithDocs.update {
                        trips
                    }
                }

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeVM" , "onCleared: Clearing VM")
        collectTripsJob?.let {
            Log.d("HomeVM" , "Cancelling Trip flow JOB")
            collectTripsJob?.cancel()
        }
    }


}