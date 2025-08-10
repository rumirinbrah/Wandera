package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.currency

import com.zzz.feature_trip.overview.domain.UICurrency

internal data class CurrencyState(
    val currencies : List<UICurrency> = emptyList(),
    val defaultCurrency : UICurrency? = null,
    val loading : Boolean = false
)
