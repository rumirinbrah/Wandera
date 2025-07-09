package com.zzz.feature_trip.overview.presentation.viewmodel

import com.zzz.data.trip.model.Day

sealed class OverviewActions {
    data class FetchTripData(val tripId : Long) : OverviewActions()
    data class NavigateToEditTrip(val tripId: Long) : OverviewActions()

    //user clicks on day through pager
    data class UpdateSelectedDay(val day: Day) : OverviewActions()
    data object ClearSelectedDay : OverviewActions()
    data class UpdateDayStatus(val dayId : Long, val done : Boolean) : OverviewActions()

    //layout
    data object ChangeItineraryLayout : OverviewActions()
    data object CleanUpResources : OverviewActions()

    //note
    data object UpdateExpenseNote : OverviewActions()
    data class OnExpenseNoteValueChange(val value : String) : OverviewActions()

    //checklist
    data class CheckChecklistItem(val itemId : Long , val checked : Boolean) : OverviewActions()
    data class DeleteChecklistItem(val itemId : Long) : OverviewActions()
    data object OnChecklistCollapse : OverviewActions()

    data class OnFabCollapse(val collapsed : Boolean) : OverviewActions()

    data object DeleteTrip : OverviewActions()
}