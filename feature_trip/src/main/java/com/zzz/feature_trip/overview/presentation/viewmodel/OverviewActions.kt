package com.zzz.feature_trip.overview.presentation.viewmodel

import com.zzz.data.trip.model.Day

sealed class OverviewActions {
    data class FetchTripData(val tripId : Long) : OverviewActions()
    //data class NavigateToEditTrip(val tripId: Long) : OverviewActions()


     //---- DAY ----
    data class UpdateDayStatus(val dayId : Long, val done : Boolean) : OverviewActions()

    //---- TODO_LOCATION ----
    data class MarkTodoAsDone(val itemId : Long , val done : Boolean) : OverviewActions()


    //---- layout ----
    data object ChangeItineraryLayout : OverviewActions()
    data object CleanUpResources : OverviewActions()


    //----note----
    data object UpdateExpenseNote : OverviewActions()
    data class OnExpenseNoteValueChange(val value : String) : OverviewActions()

    data class SelectExpenseItem(val itemId : Long?) : OverviewActions()


    //----checklist----
    data class CheckChecklistItem(val itemId : Long , val checked : Boolean) : OverviewActions()
    data class DeleteChecklistItem(val itemId : Long) : OverviewActions()
    data object OnChecklistCollapse : OverviewActions()


    data class OnFabCollapse(val collapsed : Boolean) : OverviewActions()

    data object MarkTripAsDone : OverviewActions()
    data object DeleteTrip : OverviewActions()
}