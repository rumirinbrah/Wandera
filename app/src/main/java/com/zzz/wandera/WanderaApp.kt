package com.zzz.wandera

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WanderaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WanderaApp)
            modules()
        }
    }
}