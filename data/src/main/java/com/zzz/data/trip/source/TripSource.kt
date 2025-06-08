package com.zzz.data.trip.source

import com.zzz.data.trip.TripWithDaysAndTodos
import com.zzz.data.trip.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripSource {

    //GET
    fun getTrips() : Flow<List<TripWithDaysAndTodos>>

    //get
    suspend fun getTripById(id: Long) : TripWithDaysAndTodos

    //add
    suspend fun addTrip(trip: Trip) : Long

    //update
    suspend fun updateTrip(trip: Trip)

    //delete
    suspend fun deleteTripById(id : Long)

}