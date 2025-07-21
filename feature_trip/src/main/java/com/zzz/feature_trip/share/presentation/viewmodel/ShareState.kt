package com.zzz.feature_trip.share.presentation.viewmodel

data class ShareState(
    val inProgress : Boolean = false,
    val progressMsg : String = "",
    val encodedTrip : String? = null,
    val readyToShare : Boolean = false
)
