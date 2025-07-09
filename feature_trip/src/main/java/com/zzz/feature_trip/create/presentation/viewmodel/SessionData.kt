package com.zzz.feature_trip.create.presentation.viewmodel

internal data class SessionData(
    val tripId : Long =0 ,
    val dayId : Long = 0,
    val dayIds : List<Long> = emptyList() ,
)
