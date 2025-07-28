package com.zzz.feature_trip.day_details.viewmodel

import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation

internal data class DayDetailState(
    val day : Day? = null,
    val todos : List<TodoLocation> = emptyList(),
    val loading : Boolean = false
)
