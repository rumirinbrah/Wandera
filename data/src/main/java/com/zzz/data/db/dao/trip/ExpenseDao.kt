package com.zzz.data.db.dao.trip

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zzz.data.trip.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExpenseDao {
    @Insert
    abstract suspend fun addExpense(item : ExpenseEntity) : Long

    @Update
    abstract suspend fun updateExpense(item: ExpenseEntity)

    @Query("delete from track_expense_table where id = :itemId")
    abstract suspend fun deleteExpense(itemId : Long)

    // --- GET ---
    @Query("select * from track_expense_table where id = :itemId")
    abstract suspend fun getExpenseById(itemId: Long) : ExpenseEntity

    @Query("select * from track_expense_table where tripId = :tripId")
    abstract fun getExpensesByTripId(tripId : Long) : Flow<List<ExpenseEntity>>
}