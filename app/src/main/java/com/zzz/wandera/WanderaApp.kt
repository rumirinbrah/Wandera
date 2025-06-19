package com.zzz.wandera

import android.app.Application
import com.zzz.data.di.dbModule
import com.zzz.feature_translate.di.translateModule
import com.zzz.feature_trip.di.createModule
import com.zzz.wandera.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WanderaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WanderaApp)
            modules(
                appModule ,
                createModule ,
                dbModule,
                translateModule
            )
        }
    }
}