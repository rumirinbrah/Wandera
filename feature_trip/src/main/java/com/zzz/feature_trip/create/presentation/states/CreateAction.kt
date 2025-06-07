package com.zzz.feature_trip.create.presentation.states

import android.net.Uri
import com.zzz.data.trip.model.TodoLocation

sealed class CreateAction {

    //trip
    data class OnTripTitleChange(val title: String) : CreateAction()
    data class OnDateSelect(val start: Long , val end: Long) : CreateAction()
    data class OnDocumentUpload(val docUri: Uri) : CreateAction()


    //day
    data class OnDayTitleChange(val title: String) : CreateAction()
    data class OnAddTodoLocation(val title: String , val isTodo: Boolean): CreateAction()
    data class OnDeleteTodoLocation(val todoLocation: TodoLocation): CreateAction()
    data class OnDialogVisibilityChange(val visible: Boolean): CreateAction()

    data class OnPickImage(val imageUri : Uri) : CreateAction()

    data class FetchDayById(val id: Long): CreateAction()
    data object OnSaveDay: CreateAction()
    data object OnDiscard: CreateAction()


//    data class OnTodoTitleChange(val title : String) : CreateAction()


    data object OnSave : CreateAction()
}