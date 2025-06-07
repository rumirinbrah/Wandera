package com.zzz.feature_trip.create.presentation.states

import com.zzz.data.trip.model.TodoLocation

data class DayState(
    val dayTitle : String = "" ,
    val dayNo : Int? = null ,
    val uiEnabled : Boolean = true ,
    val todoLocations : List<TodoLocation> = emptyList() ,
    val dialogVisible : Boolean = false
)
