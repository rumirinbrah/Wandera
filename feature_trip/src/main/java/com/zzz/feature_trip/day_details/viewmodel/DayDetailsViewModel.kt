package com.zzz.feature_trip.day_details.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class DayDetailsViewModel(
    private val daySource : DaySource,
    private val todoSource : TodoSource
) : ViewModel() {

    private val loggingEnabled = true

    private val _state = MutableStateFlow(DayDetailState())
    internal val state = _state.asStateFlow()

    private var collectTodosJob : Job? = null

    internal fun onAction(action : DayDetailAction){
        when(action){
            is DayDetailAction.FetchDayDetails -> {
                fetchDayDetails(action.dayId, action.collectTodosWithFlow)
            }
            is DayDetailAction.MarkTodoAsDone -> {
                markTodoAsDone(action.todoId,action.done)
            }
        }
    }

    // -------- FETCH ---------
    private fun fetchDayDetails(dayId : Long ,collectTodosByFlow : Boolean = false ){
        viewModelScope.launch {
            log {
                "fetchDayDetails : Fetch day $dayId"
            }
            _state.update {
                it.copy(loading = true)
            }
            try {
                val day = daySource.getDayById(dayId)
                _state.update {
                    it.copy(
                        day = day
                    )
                }
                fetchTodos(dayId , collectTodosByFlow)

            } catch (e : Exception) {
                e.printStackTrace()
                log {
                    "fetchDayDetails : Error"
                }
                _state.update {
                    it.copy(loading = false)
                }
            }
        }
    }
    private suspend fun fetchTodos(dayId: Long , collectTodosByFlow : Boolean = false){
        if(collectTodosByFlow){
            collectTodosFlow(dayId)
        }else{
            withContext(Dispatchers.IO){
                val todos = todoSource.getTodosByDayIdOnce(dayId)
                withContext(Dispatchers.Main){
                    _state.update {
                        it.copy(
                            todos = todos,
                            loading = false
                        )
                    }
                }
            }
        }
    }
    private fun markTodoAsDone(todoId : Long , done : Boolean){
        viewModelScope.launch {
            try {
                todoSource.markAsDone(todoId , done)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }


    // -------- FLOW ---------
    private fun collectTodosFlow(dayId: Long){
        collectTodosJob = viewModelScope.launch {
            todoSource.getTodosByDayId(dayId)
                .flowOn(Dispatchers.IO)
                .onStart {
                    log {
                        "collectTodosFlow: Start"
                    }
                    _state.update {
                        it.copy(
                            loading = false ,
                        )
                    }
                }
                .catch {
                    log {
                        "collectTodosFlow: Error"
                    }
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    log{
                        "collectTodosFlow: Finished with the flow"
                    }
                }
                .collect{newTodos->
                    _state.update {
                        it.copy(
                            todos = newTodos
                        )
                    }
                }
        }
    }


    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("dayDetailVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        collectTodosJob?.cancel()
        log {
            "Clearing vm..."
        }
    }
}