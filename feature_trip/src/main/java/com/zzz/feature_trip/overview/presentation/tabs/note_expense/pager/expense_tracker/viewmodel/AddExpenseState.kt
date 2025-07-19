package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.viewmodel

data class AddExpenseState(
    val expenseId : Long? = null,
    val amount : String = "",
    val title : String? = null,
    val expenseType : String = "other",
    val splitInto : String? = null,
    val saving : Boolean = false,
    val updating : Boolean = false,
    val loading : Boolean = false,
    val timestamp : Long = System.currentTimeMillis()
)
