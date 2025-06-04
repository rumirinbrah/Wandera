package com.zzz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zzz.data.trip.TripWithDaysAndTodos
import com.zzz.data.trip.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TripDao {

    //add
    @Insert
    abstract suspend fun insertTrip(trip: Trip)

    //update
    @Update
    abstract suspend fun updateTrip(trip: Trip)

    //delete
    @Query("DELETE from trip_table where id = :id")
    abstract suspend fun deleteTripById(id :Long)

    //GET
    @Transaction
    @Query("SELECT * from trip_table")
    abstract fun getTripsWithDaysAndTodos() : Flow<List<TripWithDaysAndTodos>>

}