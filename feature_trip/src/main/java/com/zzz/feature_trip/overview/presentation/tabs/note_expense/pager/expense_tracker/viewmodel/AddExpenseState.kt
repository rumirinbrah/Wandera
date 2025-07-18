package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.viewmodel

data class AddExpenseState(
    val amount : String = "",
    val title : String? = null,
    val expenseType : String = "other",
    val splitInto : String? = null,
    val saving : Boolean = false
)
