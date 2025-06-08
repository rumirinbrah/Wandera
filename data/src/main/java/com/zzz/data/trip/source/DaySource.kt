package com.zzz.data.trip.source

import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow

interface DaySource {

    //GET
    fun getDaysWithTodosByTripId(id : Long) : Flow<List<DayWithTodos>>

    fun getDaysByTripId(tripId : Long) : Flow<List<Day>>

    //get
    suspend fun getDayById(id: Long) : DayWithTodos

    //add
    suspend fun addDay(day: Day) : Long

    //update
    suspend fun updateDay(day: Day)
    suspend fun updateDayById(id: Long, newTitle : String)

    //delete
    suspend fun deleteDayById(id : Long)

}