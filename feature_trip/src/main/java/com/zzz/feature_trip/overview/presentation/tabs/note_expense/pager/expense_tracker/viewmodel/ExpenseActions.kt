package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.viewmodel

sealed class ExpenseActions {

    data class OnAmountChange(val amount : String) : ExpenseActions()
    data class OnTitleChange(val value : String) : ExpenseActions()
    data class OnSplitIntoChange(val splitInto : String) : ExpenseActions()
    data class OnExpenseTypeChange(val expenseType : String) : ExpenseActions()

    data object Discard : ExpenseActions()
    data class Save(val tripId : Long) : ExpenseActions()


}