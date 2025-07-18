package com.zzz.data.trip.source

import com.zzz.data.trip.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

interface ExpenseSource {

    suspend fun addExpense(item : ExpenseEntity) : Long

    suspend fun deleteExpense(itemId : Long)

    fun getExpensesByTripId(tripId : Long) : Flow<List<ExpenseEntity>>

}