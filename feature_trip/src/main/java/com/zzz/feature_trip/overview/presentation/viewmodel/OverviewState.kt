package com.zzz.feature_trip.overview.presentation.viewmodel

import com.zzz.data.note.model.ChecklistEntity
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument
import com.zzz.feature_trip.overview.domain.ExpenseEntityUI

internal data class OverviewState(
    val loading : Boolean = false,
    val trip : Trip? = null,
    val itineraryPagerLayout : Boolean = true,
    val selectedDay : DayWithTodos? = null,
    val docs : List<UserDocument> = emptyList(),
    val checklist : List<ChecklistEntity> = emptyList(),
    val checklistCollapsed : Boolean = false,
    val expenseNote : String = "",
    val expenseNoteId : Long? = null,
    val expenses : List<ExpenseEntityUI> = emptyList(),
    val totalExpense : Int? = null,
    val fabCollapsed : Boolean = true,
)
