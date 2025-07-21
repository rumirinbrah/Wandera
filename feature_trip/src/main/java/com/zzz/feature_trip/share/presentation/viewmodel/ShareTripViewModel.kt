package com.zzz.feature_trip.share.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.domain.result.Result
import com.zzz.core.domain.result.map
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.share.data.repo.TripExportManager
import com.zzz.feature_trip.share.data.repo.TripImportManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShareTripViewModel(
    private val tripSource: TripSource,
    private val daySource: DaySource,
    private val todoSource: TodoSource
) : ViewModel() {

    private lateinit var exportManager: TripExportManager
    private lateinit var importManager: TripImportManager

    private val _state = MutableStateFlow(ShareState())
    val state = _state.asStateFlow()

    fun exportTrip(tripId: Long) {
        viewModelScope.launch {
            _state.update {
                it.copy(inProgress = true)
            }
            exportManager = TripExportManager(tripSource)

            exportManager.exportTripToJson(tripId)
                //.flowOn(Dispatchers.IO)
                .onCompletion {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                }
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            println("EXPORT ERROR")
                        }

                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    progressMsg = result.data.progressMsg ,
                                    encodedTrip = result.data.exportedTrip
                                )
                            }
                        }
                    }
                }
        }
    }

    fun importTrip(encodedTripJson : String){
        viewModelScope.launch {
            _state.update {
                it.copy(inProgress = true)
            }
            importManager = TripImportManager(tripSource, daySource, todoSource)

            importManager.importTripFromJson(encodedTripJson)
                .onCompletion {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                }
                .collect{result->
                    when (result) {
                        is Result.Error -> {
                            println("EXPORT ERROR")
                        }

                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    progressMsg = result.data.progressMsg ,
                                )
                            }
                        }
                    }
                }

        }

    }


}