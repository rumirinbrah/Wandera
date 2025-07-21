package com.zzz.feature_trip.share.domain.result

data class ExportProgress(
    val done : Boolean = false,
    val progressMsg : String = "",
    val exportedTrip : String? = null
)
