package com.zzz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zzz.data.trip.model.TodoLocation
import kotlinx.coroutines.flow.Flow


@Dao
internal abstract class TodoDao {

    //add
    @Insert
    abstract suspend fun addTodo(todo : TodoLocation)

    //update
    @Update
    abstract suspend fun updateTodo(todo: TodoLocation)

    //delete
    @Query("delete from todo_location_table where id = :id")
    abstract suspend fun deleteTodo(id : Long)

    //GET
    @Transaction
    @Query("select * from todo_location_table where dayId = :dayId")
    abstract fun getTodosByDayId(dayId : Long) : Flow<List<TodoLocation>>

    @Query("select * from todo_location_table where dayId = :dayId")
    abstract fun getTodosByDayIdOnce(dayId : Long) : List<TodoLocation>

}