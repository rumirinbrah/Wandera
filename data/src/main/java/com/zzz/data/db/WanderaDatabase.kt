package com.zzz.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zzz.data.db.dao.DayDao
import com.zzz.data.db.dao.TodoDao
import com.zzz.data.db.dao.TripDao
import com.zzz.data.db.type_conv.UriTypeConverter
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip

@Database(
    entities = [
        Trip::class ,
        Day::class ,
        TodoLocation::class
    ] ,
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [UriTypeConverter::class])
internal abstract class WanderaDatabase :RoomDatabase(){

    abstract val tripDao : TripDao
    abstract val dayDao : DayDao
    abstract val todoDao : TodoDao

}