package com.zzz.feature_trip.create.presentation.states

import android.net.Uri

sealed class CreateAction {

    //trip
    sealed class TripActions : CreateAction(){
        data class OnTripTitleChange(val title: String) : TripActions()
        data class OnDateSelect(val start: Long , val end: Long) : TripActions()

        data class OnDocumentUpload(val docUri: Uri, val docName : String) : TripActions()
        data class OnDocumentUpdate(val docId : Long ,val newName : String) : TripActions()
        data class DeleteDocument(val docId : Long ):TripActions()

        data object CreateSession : TripActions()

    }

    //day
    sealed class DayActions : CreateAction(){
        data object CreateDaySession : DayActions()

        data class OnDayTitleChange(val title: String) : DayActions()
        data class OnAddTodoLocation( val title: String , val isTodo: Boolean) : DayActions()
        data class OnPickImage(val imageUri : Uri): DayActions()
        data class OnDeleteTodoLocation(val id: Long) : DayActions()

        data class OnDialogVisibilityChange(val visible: Boolean) : DayActions()

        data class FetchDayById(val id: Long): DayActions()
        data class OnDeleteDay(val id : Long) : DayActions()

        data object OnSaveDay: DayActions()
        data object OnUpdateDay : DayActions()
        data object OnDiscardCreation: DayActions()
        data object ClearDayState : DayActions()
    }

    data object OnSave : CreateAction()
    data object OnDiscardTripCreation : CreateAction()
}