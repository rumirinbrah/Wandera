package com.zzz.feature_trip.create.presentation.states

import android.net.Uri

data class TripState(
    val tripTitle : String = "",
    val startDate : Long? = null,
    val endDate : Long? = null,
    val uploadedDocs : List<Uri> = emptyList()
)

