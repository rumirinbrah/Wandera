package com.zzz.data.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.zzz.data.db.WanderaDatabase
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
}