package com.zzz.feature_trip.share.domain.models

import kotlinx.serialization.Serializable

/**
 * Entity used while sharing a Trip
 */
@Serializable
data class ExportableTripWithDays(
    val trip : ExportableTrip ,
    val days : List<ExportableDayWithTodos>
)

@Serializable
data class ExportableTrip(
    val tripName : String,
    val startDate : Long,
    val endDate : Long,
    val dateCreated : Long
)
@Serializable
data class ExportableDayWithTodos(
    val day : ExportableDay ,
    val todos : List<ExportableTodos>
)
@Serializable
data class ExportableDay(
    val locationName : String ,
    val isDone : Boolean = false ,
)
@Serializable
data class ExportableTodos(
    val title : String,
    val isTodo : Boolean = true,
)