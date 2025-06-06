package com.zzz.feature_trip.create.presentation.states

import android.net.Uri
import com.zzz.data.trip.model.TodoLocation

sealed class CreateAction {

    //trip
    data class OnTripTitleChange(val title : String) : CreateAction()
    data class OnDateSelect(val start : Long, val end : Long) : CreateAction()
    data class OnDocumentUpload(val docUri : Uri) : CreateAction()

    //day
    data class OnDayTitleChange(val title : String) : CreateAction()
    data class OnAddTodoLocation(val todo : TodoLocation) : CreateAction()
    data object OnSaveDay : CreateAction()

    data object OnSave : CreateAction()
}