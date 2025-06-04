package com.zzz.data.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.zzz.data.db.WanderaDatabase
import com.zzz.data.db.repos.DaySourceImpl
import com.zzz.data.db.repos.TodoSourceImpl
import com.zzz.data.db.repos.TripSourceImpl
import com.zzz.data.trip.DaySource
import com.zzz.data.trip.TodoSource
import com.zzz.data.trip.TripSource
import com.zzz.data.util.DbUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {

    //======= DB =========
    single<WanderaDatabase> {
        Room.databaseBuilder(
            androidContext(),
            WanderaDatabase::class.java,
            DbUtils.DB_NAME
        ).build()
    }

    //======= TRIP SRC =========
    single<TripSource> {
        val db = get<WanderaDatabase>()

        TripSourceImpl(tripDao = db.tripDao)
    }

    //======= DAY SRC =========
    single<DaySource> {
        val db = get<WanderaDatabase>()

        DaySourceImpl(dayDao = db.dayDao)
    }

    //======= TODO_LOCATION SRC =========
    single<TodoSource> {
        val db = get<WanderaDatabase>()

        TodoSourceImpl(todoDao = db.todoDao)
    }

}