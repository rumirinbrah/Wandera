package com.zzz.data.trip

import com.zzz.data.trip.model.Day
import kotlinx.coroutines.flow.Flow

interface DaySource {

    //GET
    fun getDaysByTripId(id : Long) : Flow<List<DayWithTodos>>

    //get
    suspend fun getDayById(id: Long) : DayWithTodos

    //add
    suspend fun addDay(day: Day)

    //update
    suspend fun updateDay(day: Day)

    //delete
    suspend fun deleteDayById(id : Long)

}