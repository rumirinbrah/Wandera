package com.zzz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class DayDao {

    //add
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun addDay(day: Day) : Long

    //update
    @Update
    abstract suspend fun updateDay(day: Day)

    @Query("update day_table set locationName = :newTitle where id = :id")
    abstract suspend fun updateDayById(id : Long, newTitle : String)

    //update status
    @Query("update day_table set isDone =:done where id = :dayId ")
    abstract suspend fun markDayAsDone(dayId : Long,done : Boolean)

    //delete
    @Query("delete from day_table where id = :id")
    abstract suspend fun deleteDayById(id : Long)

    //get by id
    @Transaction
    @Query("select * from day_table where id = :dayId")
    abstract suspend fun getDayWithTodos(dayId : Long) : DayWithTodos

    @Query("select * from day_table where id =:dayId")
    abstract suspend fun getDayById(dayId : Long) : Day

    //GET
    @Transaction
    @Query("select * from day_table where tripId = :tripId")
    abstract fun getDaysWithTodosByTripId(tripId : Long) : Flow<List<DayWithTodos>>

    @Query("select * from day_table where tripId = :tripId")
    abstract fun getDaysByTripId(tripId: Long) : Flow<List<Day>>

    @Query("select * from day_table where tripId = :tripId")
    abstract suspend fun getDaysByTripIdOnce(tripId : Long) : List<Day>

}