package com.zzz.data.db.repos

import com.zzz.data.db.dao.TodoDao
import com.zzz.data.trip.TodoSource
import com.zzz.data.trip.model.TodoLocation
import kotlinx.coroutines.flow.Flow

internal class TodoSourceImpl(
    private val todoDao: TodoDao
):TodoSource {

    override fun getTodosByDayId(id: Long): Flow<List<TodoLocation>> {
        return todoDao.getTodosByDayId(id)
    }

    override suspend fun addTodo(todoLocation: TodoLocation) {
        todoDao.addTodo(todoLocation)
    }

    override suspend fun updateTodo(todoLocation: TodoLocation) {
        todoDao.updateTodo(todoLocation)
    }

    override suspend fun deleteTodo(id: Long) {
        todoDao.deleteTodo(id)
    }
}