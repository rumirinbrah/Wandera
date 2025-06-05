package com.zzz.data.db.repos

import com.zzz.data.db.dao.TripDao
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

    override suspend fun getTripById(id: Long): TripWithDaysAndTodos {
        return tripDao.getTripById(id)
    }

    override suspend fun addTrip(trip: Trip) {
        tripDao.insertTrip(trip)
    }

    override suspend fun updateTrip(trip: Trip) {
        tripDao.updateTrip(trip)
    }

    override suspend fun deleteTripById(id: Long) {
        tripDao.deleteTripById(id)
    }
}