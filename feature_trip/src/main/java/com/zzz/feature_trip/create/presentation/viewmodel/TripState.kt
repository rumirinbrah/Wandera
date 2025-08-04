package com.zzz.feature_trip.create.presentation.viewmodel

import android.net.Uri
import com.zzz.data.note.model.ChecklistEntity

internal data class TripState(
    val tripId : Long = 0L ,
    val tripTitle : String = "" ,
    val startDate : Long? = null ,
    val endDate : Long? = null ,
    val selectedPhotoDoc : Uri? = null ,
    val showRenameDocDialog : Boolean = false ,
    val checklist : List<ChecklistEntity> = emptyList() ,
    val showAddChecklistDialog : Boolean = false ,
    val saving : Boolean = false ,
    val sessionOngoing : Boolean = false ,
    val isUpdating : Boolean = false
)
/*
val tripTitle : String = "",
    val startDate : Long? = null,
    val endDate : Long? = null,
    val days : List<DayWithTodos> = emptyList(),
    val uploadedDocs : List<Uri> = emptyList(),
    val saving : Boolean = false
 */
