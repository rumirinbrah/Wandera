package com.zzz.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zzz.data.trip.TripWithDays
import com.zzz.data.trip.TripWithDaysAndTodos
import com.zzz.data.trip.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class TripDao {

    //add
    @Insert
    abstract suspend fun insertTrip(trip: Trip) : Long

    //update
    @Update
    abstract suspend fun updateTrip(trip: Trip)

    //delete
    @Query("DELETE from trip_table where id = :id")
    abstract suspend fun deleteTripById(id :Long) : Int

    //GET by id
    @Transaction
    @Query("SELECT * from trip_table where id = :id")
    abstract suspend fun tripWithDaysAndTodos(id : Long) : TripWithDaysAndTodos

    @Query("select * from trip_table where id = :tripId")
    abstract suspend fun getTripById(tripId : Long) : Trip

    //GET with days and todos
    @Transaction
    @Query("SELECT * from trip_table")
    abstract fun getTrips() : Flow<List<TripWithDaysAndTodos>>

    //GET
    @Transaction
    @Query("select * from trip_table order by dateCreated DESC")
    abstract fun getTripsWithDays() : Flow<List<TripWithDays>>

}