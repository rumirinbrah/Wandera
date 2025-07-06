package com.zzz.feature_trip.overview.presentation.viewmodel

import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument

internal data class OverviewState(
    val loading : Boolean = false,
    val trip : Trip? = null,
    val itineraryPagerLayout : Boolean = true,
    val selectedDay : DayWithTodos? = null,
    val docs : List<UserDocument> = emptyList(),
    val expenseNote : String = "",
    val expenseNoteId : Long? = null
)
