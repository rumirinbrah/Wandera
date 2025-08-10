package com.zzz.feature_trip.overview.presentation.viewmodel

import android.content.Intent
import com.zzz.core.presentation.events.UIEvents

internal sealed interface OverviewEvents {
    data class Error(val errorMsg : String) : OverviewEvents

    data class SuccessWithMsg(val msg : String) : OverviewEvents

    data class ShareExpenseIntent(val intent: Intent) : OverviewEvents

    data object NavigateUp : OverviewEvents
}