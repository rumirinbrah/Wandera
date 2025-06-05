package com.zzz.feature_trip.create.presentation.states

import com.zzz.data.trip.model.TodoLocation

data class DayState(
    val dayTitle : String = "",
    val todoLocations : List<TodoLocation> = emptyList()
)
