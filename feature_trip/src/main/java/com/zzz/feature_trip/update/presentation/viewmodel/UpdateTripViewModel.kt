package com.zzz.feature_trip.update.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.source.UserDocSource
import com.zzz.feature_trip.create.presentation.viewmodel.CreateAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class UpdateTripViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val todoSource: TodoSource ,
    private val docSource : UserDocSource
) :ViewModel(){

    private var loggingEnabled  : Boolean = true
    private var sessionOngoing  : Boolean = false

    private var dayId : Long = -1L

    private val _state = MutableStateFlow(UpdateTripState())
    internal val state = _state.asStateFlow()

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    private var collectTodosJob: Job? = null

    init {
        log {
            "VM init..."
        }
    }

    fun onAction(action: CreateAction) {
        when (action) {

            //=========== TRIP ===========
            is CreateAction.TripActions -> {
                when (action) {

                    // date
                    is CreateAction.TripActions.OnDateSelect -> {
                        onDateSelect(action.start , action.end)
                    }
                    //doc
                    is CreateAction.TripActions.OnDocumentUpload -> {
                        uploadDocument(action.docUri,action.docName)
                    }
                    is CreateAction.TripActions.OnDocumentUpdate->{
                        updateDocument(action.docId,action.newName)
                    }
                    is CreateAction.TripActions.DeleteDocument->{
                        deleteDocument(action.docId)
                    }
                    //title
                    is CreateAction.TripActions.OnTripTitleChange -> {
                        onTripTitleChange(action.title)
                    }

                    is CreateAction.TripActions.FetchTripData->{
                        fetchTripData(action.tripId)
                    }
                    else->Unit

                }
            }
            is CreateAction.DayActions.OnDeleteDay -> {
                deleteDayById(action.id)
            }

            CreateAction.OnSave -> {
                saveTrip()
            }
            else->Unit

        }
    }

    private fun fetchTripData(tripId : Long){
        if(sessionOngoing){
            return
        }
        viewModelScope.launch {
            sessionOngoing = true
            val trip = tripSource.getTripById(tripId)
            _state.update {
                it.copy(
                    tripId = tripId,
                    tripTitle = trip.tripName,
                    startDate =trip.startDate ,
                    endDate = trip.endDate
                )
            }
            getTripDaysFlow()
            getUserDocsFlow()
        }
    }

    //=========== DAY ===========

    /**
     * Delete Day entity by id
     */
    private fun deleteDayById(dayId : Long){
        viewModelScope.launch {
            daySource.deleteDayById(dayId)
        }
    }

    //=========== TRIP ===========
    private fun onTripTitleChange(title: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(tripTitle = title)
            }
        }
    }

    //TODO - Add constraints
    private fun onDateSelect(start: Long , end: Long) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    startDate = start ,
                    endDate = end
                )
            }
        }
    }

    private fun uploadDocument(docUri: Uri , docName : String) {
        val tripId= _state.value.tripId ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val doc = UserDocument(
                    docName = docName,
                    uri= docUri,
                    tripId = tripId
                )
                val id = docSource.addDocument(doc)
                log{
                    "uploadDocument: Added doc $docName $id"
                }

            }

        }
    }
    private fun deleteDocument(docId : Long){
        viewModelScope.launch {
            docSource.deleteDocumentById(docId)
        }
    }
    private fun updateDocument(docId : Long , newName : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                docSource.updateDocumentById(docId,newName)
            }
        }
    }



    //========= SAVE =========
    private fun saveTrip() {
        viewModelScope.launch {
            val isValid = validateUserInput(
                tripNameEmpty = {
                    _events.trySend(UIEvents.Error("Please enter a trip name"))
                } ,
                tripDatesNull = {
                    _events.trySend(UIEvents.Error("Please select valid start and end dates"))
                }
            )
            val tripId = _state.value.tripId ?: return@launch

            if (!isValid) {
                log {
                    "saveTrip: Invalid input, returning"
                }
                return@launch
            }
            log {
                "saving..."
            }
            _state.update {
                it.copy(saving = true)
            }

            withContext(Dispatchers.IO) {
                val trip = Trip(
                    id = tripId ,
                    tripName = _state.value.tripTitle.trim() ,
                    startDate = _state.value.startDate!! ,
                    endDate = _state.value.endDate!! ,
                )
                tripSource.updateTrip(trip)
                log {
                    "Saved, id $tripId"
                }

            }
            withContext(Dispatchers.Main.immediate){
                delay(1000L)
                _state.update {
                    it.copy(saving = false)
                }
                _events.send(UIEvents.Success)
            }
            resetTripState()
        }
    }

    /**
     * Validates trip title & the dates
     */
    private fun validateUserInput(
        tripNameEmpty: () -> Unit ,
        tripDatesNull: () -> Unit ,
    ): Boolean {
        val trip = _state.value
        //trip name
        if (trip.tripTitle.isBlank()) {
            tripNameEmpty()
            return false
        }
        if (trip.startDate == null || trip.endDate == null) {
            tripDatesNull()
            return false
        }
        return true

    }

    /**
     * Resets trip state, day state & session data
     */
    private fun resetTripState() {
        viewModelScope.launch {
            _state.update { UpdateTripState() }
        }
    }


    //DB GET REQs
    //Days flow
    private fun getTripDaysFlow() {
        viewModelScope.launch {
            if (_state.value.tripId == null) {
                log {
                    "getTripDaysFlow: Invalid session ID, returning..."
                }
                return@launch
            }
            daySource.getDaysByTripId(_state.value.tripId!!)
                .flowOn(Dispatchers.IO)
                .catch {
                    log {
                        "getTripDaysFlow: Error"
                    }
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    if(it != null && it is CancellationException){
                        throw it
                    }
                    log{
                        "getTripDaysFlow: Flow completed"
                    }

                }
                .collect { days ->
                    _state.update {
                        it.copy(
                            days = days
                        )
                    }
                }
        }
    }

    //Docs flow
    private fun getUserDocsFlow(){
        val tripId = _state.value.tripId ?: return
        viewModelScope.launch {
            docSource.getUserDocumentsByTripId(tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    log {
                        "User docs flow error"
                    }
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.message?.let { errorMsg->
                            _events.trySend(UIEvents.Error(errorMsg))
                        }
                        it.printStackTrace()
                    }
                }
                .collect{newDocs->
                    _state.update {
                        it.copy(
                            userDocs = newDocs
                        )
                    }

                }
        }
    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("updateTripVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        log {
            "Clearing vm..."
        }
    }


}