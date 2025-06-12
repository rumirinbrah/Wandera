package com.zzz.feature_trip.overview.presentation

sealed class OverviewActions {
    data class FetchTripData(val tripId : Long) : OverviewActions()
    data class NavigateToEditTrip(val tripId: Long) : OverviewActions()
}