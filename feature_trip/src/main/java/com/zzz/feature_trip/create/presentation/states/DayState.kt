package com.zzz.feature_trip.create.presentation.states

import android.net.Uri
import com.zzz.data.trip.model.TodoLocation

internal data class DayState(
    val dayTitle : String = "" ,
    val image : Uri? = null,
    val isUpdating : Boolean = false ,
    //val todoLocations : List<TodoLocation> = emptyList() ,
    val dialogVisible : Boolean = false
)
