package com.zzz.feature_trip.overview.presentation

import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument

internal data class OverviewState(
    val loading : Boolean = false,
    val trip : Trip? = null,
    val days : List<Day> = emptyList(),
    val docs : List<UserDocument> = emptyList()
)
