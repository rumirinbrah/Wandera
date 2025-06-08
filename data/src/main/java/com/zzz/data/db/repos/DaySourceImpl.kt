package com.zzz.data.db.repos

import com.zzz.data.db.dao.DayDao
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow


internal class DaySourceImpl(
    private val dayDao: DayDao
) : DaySource {

    override fun getDaysWithTodosByTripId(id: Long): Flow<List<DayWithTodos>> {
        return dayDao.getDaysWithTodosByTripId(id)
    }

    override fun getDaysByTripId(tripId: Long): Flow<List<Day>> {
        return dayDao.getDaysByTripId(tripId)
    }

    override suspend fun getDayById(id: Long): DayWithTodos {
        return dayDao.getDayById(id)
    }

    override suspend fun addDay(day: Day) : Long {
        return dayDao.addDay(day)
    }

    override suspend fun updateDay(day: Day) {
        dayDao.updateDay(day)
    }

    override suspend fun updateDayById(id: Long , newTitle: String) {
        dayDao.updateDayById(id,newTitle)
    }

    override suspend fun deleteDayById(id: Long) {
        dayDao.deleteDayById(id)
    }
}