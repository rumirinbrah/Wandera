package com.zzz.feature_trip.home.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.common.SettingsPreferences
import com.zzz.data.trip.TripWithDays
import com.zzz.data.trip.source.TripSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel(
    private val tripSource: TripSource,
    context: Context
) : ViewModel() {

    private val settingsPref = SettingsPreferences(context)


    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(HomeState())
    internal val state = _state.asStateFlow()

    private val _tripWithDocs = MutableStateFlow<List<TripWithDays>>(emptyList())
    val tripWithDocs = _tripWithDocs.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _tripWithDocs.value
    )

    private var collectTripsJob : Job? = null

    init {
        _state.update {
            it.copy(
                homeItemTicketLikeContainer = settingsPref.isHomeContainerTicket()
            )
        }
        getTripsFlow()
    }

    //collect
    private fun getTripsFlow(){
        collectTripsJob = viewModelScope.launch {

            _state.update {
                it.copy(loading = true)
            }

            tripSource.getTripsWithUserDocs()
                .onStart {
                    _state.update {
                        it.copy(loading = false)
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch {
                    _state.update {homeState->
                        homeState.copy(loading = false)
                    }
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