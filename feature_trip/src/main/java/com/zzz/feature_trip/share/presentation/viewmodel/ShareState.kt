package com.zzz.feature_trip.share.presentation.viewmodel

/**
 * @param inProgress Represents if data processing is in progress
 * @param progressMsg Msg to be shown to the user while data is being processed
 * @param encodedTrip Trip encoded to JSON
 * @param readyToShare Whether processing is done
 * @param primaryButtonVisible Whether to display the primary action button, i.e Share Trip, Import Trip
 */
internal data class ShareState(
    val inProgress : Boolean = false,
    val progressMsg : String = "",
    val encodedTrip : String? = null,
    val readyToShare : Boolean = false,
    val primaryButtonVisible : Boolean = true
)
