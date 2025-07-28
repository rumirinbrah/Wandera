package com.zzz.feature_trip.overview.presentation.viewmodel

import com.zzz.core.presentation.events.UIEvents

internal sealed interface OverviewEvents {
    data class Error(val errorMsg : String) : OverviewEvents

    data class SuccessWithMsg(val msg : String) : OverviewEvents

    data object NavigateUp : OverviewEvents
}