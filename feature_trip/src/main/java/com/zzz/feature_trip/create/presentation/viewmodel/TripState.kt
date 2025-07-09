package com.zzz.feature_trip.create.presentation.viewmodel

import com.zzz.data.note.model.ChecklistEntity

internal data class TripState(
    val tripId : Long = 0L,
    val tripTitle : String = "",
    val startDate : Long? = null,
    val endDate : Long? = null,
    val checklist : List<ChecklistEntity> = emptyList(),
    val showAddChecklistDialog : Boolean = false,
    val saving : Boolean = false,
    val sessionOngoing : Boolean = false
)
/*
val tripTitle : String = "",
    val startDate : Long? = null,
    val endDate : Long? = null,
    val days : List<DayWithTodos> = emptyList(),
    val uploadedDocs : List<Uri> = emptyList(),
    val saving : Boolean = false
 */
