package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel

/**
 * @param expenseId Id of the selected item, if any
 * @param currencySymbol Symbol of the selected currency, for Ex, $
 * @param amount Amount entered
 * @param expenseType Type of expense like Travel, Food
 */
internal data class AddExpenseState(
    val expenseId : Long? = null,
    val currencySymbol : String = "$",
    val amount : String = "",
    val title : String? = null,
    val expenseType : String = "other",
    val splitInto : String? = null,
    val saving : Boolean = false,
    val updating : Boolean = false,
    val loading : Boolean = false,
    val timestamp : Long = System.currentTimeMillis()
)
