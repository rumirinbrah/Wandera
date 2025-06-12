package com.zzz.data.db.repos

import com.zzz.data.db.dao.TripDao
import com.zzz.data.trip.TripWithDays
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.TripWithDaysAndTodos
import com.zzz.data.trip.model.Trip
import kotlinx.coroutines.flow.Flow

internal class TripSourceImpl(
    private val tripDao: TripDao
) : TripSource {

    override fun getTrips(): Flow<List<TripWithDaysAndTodos>> {
        return tripDao.getTrips()
    }

    override fun getTripsWithUserDocs(): Flow<List<TripWithDays>> {
        return tripDao.getTripsWithDays()
    }

    override suspend fun getTripWithDaysAndTodosById(id: Long): TripWithDaysAndTodos {
        return tripDao.tripWithDaysAndTodos(id)
    }

    override suspend fun getTripById(tripId: Long): Trip {
        return tripDao.getTripById(tripId)
    }

    override suspend fun addTrip(trip: Trip) :Long{
        return tripDao.insertTrip(trip)
    }

    override suspend fun updateTrip(trip: Trip) {
        tripDao.updateTrip(trip)
    }

    override suspend fun deleteTripById(id: Long) : Int {
        return tripDao.deleteTripById(id)
    }
}