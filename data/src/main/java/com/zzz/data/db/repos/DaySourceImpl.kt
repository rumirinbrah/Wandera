package com.zzz.data.db.repos

import com.zzz.data.db.dao.DayDao
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow


internal class DaySourceImpl(
    private val dayDao: DayDao
) : DaySource {

    //GET
    override fun getDaysWithTodosByTripId(id: Long): Flow<List<DayWithTodos>> {
        return dayDao.getDaysWithTodosByTripId(id)
    }

    override fun getDaysByTripId(tripId: Long): Flow<List<Day>> {
        return dayDao.getDaysByTripId(tripId)
    }

    override suspend fun getDayWithTodosById(id: Long): DayWithTodos {
        return dayDao.getDayWithTodos(id)
    }

    override suspend fun getDayById(dayId: Long): Day {
        return dayDao.getDayById(dayId)
    }

    override suspend fun getDaysByTripIdOnce(tripId: Long): List<Day> {
        return dayDao.getDaysByTripIdOnce(tripId)
    }

    //add
    override suspend fun addDay(day: Day) : Long {
        return dayDao.addDay(day)
    }

    //updates
    override suspend fun updateDay(day: Day) {
        dayDao.updateDay(day)
    }

    override suspend fun updateDayById(id: Long , newTitle: String) {
        dayDao.updateDayById(id,newTitle)
    }

    override suspend fun markDayAsDone(dayId: Long , done: Boolean) {
        dayDao.markDayAsDone(dayId,done)
    }

    override suspend fun deleteDayById(id: Long) {
        dayDao.deleteDayById(id)
    }
}