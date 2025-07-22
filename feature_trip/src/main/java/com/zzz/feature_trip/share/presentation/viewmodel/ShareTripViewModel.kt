package com.zzz.feature_trip.share.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.domain.result.Result
import com.zzz.core.domain.result.map
import com.zzz.core.platform.files.WanderaFileManager
import com.zzz.core.platform.files.util.FileManagerError
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.share.data.repo.TripExportManager
import com.zzz.feature_trip.share.data.repo.TripImportManager
import com.zzz.feature_trip.share.domain.models.ShareEvents
import com.zzz.feature_trip.share.domain.result.ExportError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShareTripViewModel(
    private val tripSource: TripSource,
    private val daySource: DaySource,
    private val todoSource: TodoSource,
    private val context: Context
) : ViewModel() {

    private val loggingEnabled = true

    private lateinit var exportManager: TripExportManager
    private lateinit var importManager: TripImportManager

    private val _state = MutableStateFlow(ShareState())
    internal val state = _state.asStateFlow()

    private val _events = Channel<ShareEvents>()
    internal val events = _events.receiveAsFlow()



    fun exportTrip(tripId: Long) {
        if(_state.value.inProgress){
            return
        }
        viewModelScope.launch {
            log {
                "Exporting trip..."
            }

            _state.update {
                it.copy(inProgress = true)
            }
            exportManager = TripExportManager(tripSource)


            exportManager.exportTripToJson(tripId)
                .onCompletion {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    log {
                        "exportTripToJson flow complete"
                    }
                }
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            when(val error = result.error){
                                is ExportError.Error ->{
                                    _state.update {
                                        it.copy(
                                            progressMsg = error.errorMsg ,
                                            encodedTrip = null
                                        )
                                    }
                                }
                            }
                            log {
                                "exportTrip : Error"
                            }

                        }

                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    progressMsg = result.data.progressMsg ,
                                    encodedTrip = result.data.exportedTrip
                                )
                            }
                            result.data.exportedTrip?.let{
                                handleEncodedJson(it)
                            }
                        }
                    }
                }
        }
    }

    private fun handleEncodedJson(encodedJson : String){
        viewModelScope.launch {
            log {
                "handleEncodedJson : Handling encoded JSON..."
            }
            _state.update {
                it.copy(
                    inProgress = true
                )
            }
            val fileManager = WanderaFileManager()

            val result = fileManager.writeJsonToFile(
                context,
                json = encodedJson,
                tripName = "JAPAAAAAN_HARDCODE"
            )
            when(result){
                is Result.Error -> {
                    when(val error =result.error){
                        is FileManagerError.WriteError -> {
                            _state.update {
                                it.copy(
                                    inProgress = false,
                                    progressMsg = error.errorMsg
                                )
                            }
                        }
                    }

                }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            inProgress = false,
                            progressMsg = "Export successful"
                        )
                    }
                    result.data.fileUri?.let {
                        shareJsonEncodedTrip(it, "JAPAAAAAN_HARDCODE")
                    }
                }
            }


        }
    }

    fun exportTripJsonFromUri(jsonUri : Uri){
        log {
            "exportTripJsonFromUri : Exporting json from URI"
        }
        viewModelScope.launch {
            _state.update {
                it.copy(inProgress = true)
            }
            val fileManager = WanderaFileManager()
            val result = fileManager.readTripJsonFromUri(context,jsonUri)
            when(result){
                is Result.Error -> {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    log {
                        "exportTripJsonFromUri : Error..."
                    }
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    log {
                        "exportTripJsonFromUri : Success..."
                    }
                    result.map {jsonString->
                        jsonString?.let {
                            importTrip(jsonString)
                        }
                    }
                }
            }
        }
    }
    fun importTrip(encodedTripJson : String){
//        if(_state.value.inProgress){
//            return
//        }
        viewModelScope.launch {
            log {
                "importing trip..."
            }

            _state.update {
                it.copy(inProgress = true)
            }
            importManager = TripImportManager(tripSource, daySource, todoSource)

            importManager.importTripFromJson(encodedTripJson)
                .onCompletion {
                    _state.update {
                        it.copy(
                            inProgress = false,
                            primaryButtonVisible = false
                        )
                    }
                    log {
                        "importTripFromJson : on complete"
                    }
                }
                .collect{result->
                    when (result) {
                        is Result.Error -> {
                            when(val error = result.error){
                                is ExportError.Error ->{
                                    _state.update {
                                        it.copy(
                                            progressMsg = error.errorMsg ,
                                            encodedTrip = null
                                        )
                                    }
                                }
                            }
                            log {
                                "importTrip : Error"
                            }
                        }

                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    progressMsg = result.data.progressMsg ,
                                    inProgress = result.data.inProgress
                                )
                            }
                        }
                    }
                }

        }

    }
    private suspend fun shareJsonEncodedTrip(fileUri : Uri, tripName : String){
        log {
            "shareJsonEncodedTrip : Creating intent"
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"

            putExtra(Intent.EXTRA_STREAM , fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            //--- subject ---
            putExtra(Intent.EXTRA_SUBJECT,"Hey, here's my trip to $tripName")
            putExtra(Intent.EXTRA_TEXT, "Here's a detailed itinerary for $tripName, open it with Wandera to save it!")
        }
        val chooserIntent = Intent.createChooser(shareIntent,"Share Trip via...")
        log {
            "shareJsonEncodedTrip : Created intent"
        }
//        context.startActivity(chooserIntent)
        _events.send(ShareEvents.IntentGenerated(chooserIntent))
        log {
            "shareJsonEncodedTrip : Shared intent to UI!"
        }
    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("shareVm" , msg())
        }
    }


}