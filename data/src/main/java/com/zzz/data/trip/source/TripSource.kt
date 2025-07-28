package com.zzz.data.trip.source

import com.zzz.data.trip.TripWithDays
import com.zzz.data.trip.TripWithDaysAndTodos
import com.zzz.data.trip.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripSource {

    //add
    suspend fun addTrip(trip: Trip) : Long

    //update
    suspend fun updateTrip(trip: Trip)

    suspend fun markTripAsDone(markAsDone : Boolean , tripId: Long)

    //delete
    suspend fun deleteTripById(id : Long) : Int

    //---GET---

    fun getTrips() : Flow<List<TripWithDaysAndTodos>>
    fun getFinishedTripWithDays() : Flow<List<TripWithDays>>
    fun getTripsWithDays() : Flow<List<TripWithDays>>
    suspend fun getTripWithDaysAndTodosById(id: Long) : TripWithDaysAndTodos
    suspend fun getTripById(tripId : Long) : Trip
    suspend fun getTripNameById(tripId: Long) : String?

}