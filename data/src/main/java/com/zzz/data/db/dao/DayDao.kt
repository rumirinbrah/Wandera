package com.zzz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DayDao {

    //add
    @Insert
    abstract suspend fun addDay(day: Day)

    //update
    @Update
    abstract suspend fun updateDay(day: Day)

    //delete
    @Query("delete from day_table where id = :id")
    abstract suspend fun deleteDayById(id : Long)

    //GET
    @Transaction
    @Query("select * from day_table where tripId = :tripId")
    abstract fun getDaysWithTodosByTripId(tripId : Long) : Flow<List<DayWithTodos>>

}