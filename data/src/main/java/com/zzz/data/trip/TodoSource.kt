package com.zzz.data.trip

import com.zzz.data.trip.model.TodoLocation
import kotlinx.coroutines.flow.Flow

interface TodoSource {

    //GET
    fun getTodosByDayId(id : Long) : Flow<List<TodoLocation>>

    //get
    //suspend fun getDayById(id: Long) : DayWithTodos

    //add
    suspend fun addTodo(todoLocation: TodoLocation)

    //update
    suspend fun updateTodo(todoLocation: TodoLocation)

    //delete
    suspend fun deleteTodo(id : Long)

}