package com.zzz.feature_trip.create.presentation.states

import android.net.Uri
import com.zzz.data.trip.model.Day

data class TripState(
    val tripTitle : String = "",
    val startDate : Long? = null,
    val endDate : Long? = null,
    val days : List<Day> = emptyList(),
    val uploadedDocs : List<Uri> = emptyList()
)

