package com.zzz.feature_trip.recents.presentation.viewmodel

import com.zzz.data.trip.TripWithDays

internal data class RecentTripsState(
    val recents : List<TripWithDays> = emptyList(),
    val loading : Boolean = false
)
