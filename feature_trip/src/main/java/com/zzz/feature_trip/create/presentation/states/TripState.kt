package com.zzz.feature_trip.create.presentation.states

import android.net.Uri
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day

data class TripState(
    val tripTitle : String = "",
    val startDate : Long? = null,
    val endDate : Long? = null,
    val days : List<DayWithTodos> = emptyList(),
    val uploadedDocs : List<Uri> = emptyList()
)

