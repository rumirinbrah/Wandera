package com.zzz.data.di

import androidx.room.Room
import com.zzz.data.db.WanderaDatabase
import com.zzz.data.db.repos.DaySourceImpl
import com.zzz.data.db.repos.TodoSourceImpl
import com.zzz.data.db.repos.TripSourceImpl
import com.zzz.data.db.repos.UserDocSourceImpl
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.source.UserDocSource
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

    //======= DOC SRC =========
    single<UserDocSource> {
        val db = get<WanderaDatabase>()
        UserDocSourceImpl(userDocDao = db.userDocDao)
    }

}