package com.zzz.feature_trip.overview.presentation.viewmodel

import com.zzz.data.note.model.ChecklistEntity
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument
import com.zzz.feature_trip.overview.domain.ExpenseEntityUI
import java.time.LocalDate

/**
 * @param loading Loading
 * @param trip Selected Trip
 * @param itineraryPagerLayout Whether itinerary layout is pager or lazy list
 * @param selectedDay Currrently selected day
 * @param docs User docs
 * @param checklist Checklist
 * @param checklistCollapsed Control collapsed list
 * @param trapeziumChecklist Shape of checklist containers
 * @param expenseNote
 * @param expenses
 * @param selectedExpenseId To set currently selected expense
 * @param totalExpense Total expense so far
 * @param fabCollapsed Control multi option FAB
 * @param playMarkAsDoneAnimation To play animation when trip marked as done
 */
internal data class OverviewState(
    val loading : Boolean = false,
    val trip : Trip? = null,
    val itineraryPagerLayout : Boolean = true,
    val docs : List<UserDocument> = emptyList(),
    val checklist : List<ChecklistEntity> = emptyList(),
    val checklistCollapsed : Boolean = false,
    val trapeziumChecklist : Boolean = true,
    val expenseNote : String = "",
    val expenseNoteId : Long? = null,
    val expenses : List<ExpenseEntityUI> = emptyList(),
    val selectedExpenseId : Long? = null,
    val groupedExpenses : Map<LocalDate , List<ExpenseEntityUI>> = emptyMap(),
    val totalExpense : Int? = null,
    val fabCollapsed : Boolean = true,
    val playMarkAsDoneAnimation : Boolean = false
)
