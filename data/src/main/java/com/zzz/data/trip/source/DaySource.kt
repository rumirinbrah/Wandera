package com.zzz.data.trip.source

import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow

interface DaySource {

    //GET
    fun getDaysWithTodosByTripId(id : Long) : Flow<List<DayWithTodos>>

    fun getDaysByTripId(tripId : Long) : Flow<List<Day>>

    suspend fun getDaysByTripIdOnce(tripId: Long) : List<Day>

    //get
    suspend fun getDayWithTodosById(id: Long) : DayWithTodos

    suspend fun getDayById(dayId : Long) : Day

    //add
    suspend fun addDay(day: Day) : Long

    //update
    suspend fun updateDay(day: Day)
    suspend fun updateDayById(id: Long, newTitle : String)
    suspend fun markDayAsDone(dayId: Long,done : Boolean)

    //delete
    suspend fun deleteDayById(id : Long)

}