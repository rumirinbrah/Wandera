package com.zzz.data.db.repos

import com.zzz.data.db.dao.DayDao
import com.zzz.data.trip.DaySource
import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow


internal class DaySourceImpl(
    private val dayDao: DayDao
) :DaySource{

    override fun getDaysByTripId(id: Long): Flow<List<DayWithTodos>> {
        return dayDao.getDaysWithTodosByTripId(id)
    }

    override suspend fun getDayById(id: Long): DayWithTodos {
        return dayDao.getDayById(id)
    }

    override suspend fun addDay(day: Day) {
        dayDao.addDay(day)
    }

    override suspend fun updateDay(day: Day) {
        dayDao.updateDay(day)
    }

    override suspend fun deleteDayById(id: Long) {
        dayDao.deleteDayById(id)
    }
}