package com.zzz.feature_trip.overview.presentation

import com.zzz.data.trip.model.Day

sealed class OverviewActions {
    data class FetchTripData(val tripId : Long) : OverviewActions()
    data class NavigateToEditTrip(val tripId: Long) : OverviewActions()
    //user clicks on day through pager
    data class UpdateSelectedDay(val day: Day) : OverviewActions()
    data object ClearSelectedDay : OverviewActions()
}