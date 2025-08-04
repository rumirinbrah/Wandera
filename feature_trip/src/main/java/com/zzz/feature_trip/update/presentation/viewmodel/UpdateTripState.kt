package com.zzz.feature_trip.update.presentation.viewmodel

import android.net.Uri
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.UserDocument

internal data class UpdateTripState(
    val tripTitle : String = "" ,
    val tripId : Long? = null,
    val startDate : Long? = null ,
    val endDate : Long? = null ,
    val dayTitle : String = "" ,
    val image : Uri? = null ,
    val selectedPhotoDoc : Uri? = null,
    val days : List<Day> = emptyList() ,
    val todos : List<TodoLocation> = emptyList() ,
    val userDocs : List<UserDocument> = emptyList() ,
    val saving : Boolean = false ,
)
