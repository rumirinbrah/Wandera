package com.zzz.data.db.repos.trip

import com.zzz.data.db.dao.trip.ExpenseDao
import com.zzz.data.trip.model.ExpenseEntity
import com.zzz.data.trip.source.ExpenseSource
import kotlinx.coroutines.flow.Flow

class ExpenseSrcImpl(
    private val dao : ExpenseDao
) : ExpenseSource {

    override suspend fun addExpense(item: ExpenseEntity): Long {
        return dao.addExpense(item)
    }

    override suspend fun updateExpense(item: ExpenseEntity) {
        dao.updateExpense(item)
    }

    override suspend fun deleteExpense(itemId: Long) {
        dao.deleteExpense(itemId)
    }

    override suspend fun getExpenseById(itemId: Long): ExpenseEntity {
        return dao.getExpenseById(itemId)
    }

    override fun getExpensesByTripId(tripId: Long): Flow<List<ExpenseEntity>> {
        return dao.getExpensesByTripId(tripId)
    }
}