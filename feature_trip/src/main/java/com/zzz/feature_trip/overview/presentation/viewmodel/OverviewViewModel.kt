package com.zzz.feature_trip.overview.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.source.UserDocSource
import com.zzz.feature_trip.overview.data.local.ItineraryLayoutPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class OverviewViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val todoSource: TodoSource ,
    private val docSource: UserDocSource,
    context: Context
) : ViewModel() {

    private val layoutPref = ItineraryLayoutPref(context)
    private var sessionOnGoing = false

    private val _overviewState = MutableStateFlow(OverviewState())
    internal val overviewState = _overviewState.asStateFlow()

    private val _days = MutableStateFlow<List<Day>>(emptyList())
    val days = _days.stateIn(
        viewModelScope ,
        SharingStarted.WhileSubscribed(5000L) ,
        _days.value
    )

    private var collectDaysJob: Job? = null

    init {
        Log.d("overviewVm" , "Init....")
        _overviewState.update {
            it.copy(
                itineraryPagerLayout = layoutPref.isLayoutPager()
            )
        }
    }

    fun onAction(action: OverviewActions) {
        when (action) {
            is OverviewActions.FetchTripData -> {
                fetchTripData(action.tripId)
            }

            is OverviewActions.NavigateToEditTrip -> {

            }

            is OverviewActions.UpdateSelectedDay -> {
                updateSelectedDay(action.day)
            }

            is OverviewActions.UpdateDayStatus -> {
                updateDayStatus(action.dayId , action.done)
            }

            OverviewActions.ClearSelectedDay -> {
                clearSelectedDay()
            }

            OverviewActions.ChangeItineraryLayout -> {
                changeItineraryLayout()
            }

            OverviewActions.CleanUpResources -> cleanUpResources()

            //dELETE
            OverviewActions.DeleteTrip ->{
                deleteTrip()
            }
        }
    }

    //fetch
    private fun fetchTripData(tripId: Long) {
        if (sessionOnGoing) {
            return
        }
        viewModelScope.launch {
            Log.d("overviewVM" , "fetchTripData: Fetching trip...")

            _overviewState.update {
                it.copy(loading = true)
            }

            try {
                val trip = tripSource.getTripById(tripId)
                val docs = docSource.getUserDocumentsByTripIdOnce(tripId)
                _overviewState.update {
                    it.copy(
                        trip = trip ,
                        docs = docs,
                        loading = false
                    )
                }
                collectDaysFlow(tripId)
                sessionOnGoing = true


            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }

        }
    }

    //update selected day
    private fun updateSelectedDay(day: Day) {
        viewModelScope.launch {

            _overviewState.update {
                it.copy(
                    loading = true
                )
            }
            withContext(Dispatchers.IO) {
                val todos = todoSource.getTodosByDayIdOnce(day.id)
                val dayWithTodos = DayWithTodos(
                    day = day ,
                    todosAndLocations = todos
                )
                withContext(Dispatchers.Main) {
                    _overviewState.update {
                        it.copy(
                            loading = false ,
                            selectedDay = dayWithTodos
                        )
                    }
                }
            }


        }
    }

    //clear selected day
    private fun clearSelectedDay() {
        viewModelScope.launch {
            delay(500)
            _overviewState.update {
                it.copy(selectedDay = null)
            }
        }
    }

    private fun updateDayStatus(dayId: Long , isDone: Boolean) {
        viewModelScope.launch {
            Log.d("overviewVM" , "updateDayStatus: Marking $dayId as $isDone")

            daySource.markDayAsDone(dayId , isDone)
        }
    }

    //layout
    private fun changeItineraryLayout() {
        viewModelScope.launch {
            _overviewState.update {
                layoutPref.setPagerLayout(
                    !it.itineraryPagerLayout
                )
                it.copy(itineraryPagerLayout = !it.itineraryPagerLayout)
            }
        }
    }

    //flows
    private fun collectDaysFlow(tripId: Long) {
        collectDaysJob = viewModelScope.launch {
            daySource.getDaysByTripId(tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("overviewVM" , "collectDaysFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
//                        it.message?.let { errorMsg->
//                            _events.trySend(UIEvents.Error(errorMsg))
//                        }
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    if (it is CancellationException) {
                        throw it
                    }
                    Log.d("overviewVM" , "collectDaysFlow: Finished with the flow")
                }
                .collect { newDays ->
                    Log.d("overviewVM" , "collectDaysFlow: NEW DAYS SIZE IS ${newDays.size}")

                    _days.update {
                        newDays
                    }
                }
        }
    }

    //!!!DELETE
    private fun deleteTrip(){
        viewModelScope.launch {

            val tripId = _overviewState.value.trip?.id ?: return@launch

            _overviewState.update {
                it.copy(loading = true)
            }
            cleanUpResources()
            tripSource.deleteTripById(tripId)
            _overviewState.update {
                it.copy(loading = false)
            }
        }
    }

    //clean up
    private fun cleanUpResources() {
        viewModelScope.launch {
            delay(200)
            collectDaysJob?.let {
                Log.d("overviewVM" , "cleanUpResources: Cancelling days job")
                it.cancel()
            }
            sessionOnGoing = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("overviewVM" , "onCleared: Clearing vm.....")
        collectDaysJob?.let {
            Log.d("overviewVM" , "onCleared: Cancelling days job")
            it.cancel()
        }

    }


}