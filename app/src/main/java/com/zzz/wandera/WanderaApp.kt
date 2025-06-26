package com.zzz.wandera

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import com.zzz.core.di.coreModule
import com.zzz.core.platform.notification.NotificationUtil
import com.zzz.data.di.dbModule
import com.zzz.feature_translate.di.translateModule
import com.zzz.feature_trip.di.createModule
import com.zzz.wandera.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WanderaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createWanderaNotificationChannel()

        startKoin {
            androidContext(this@WanderaApp)
            modules(
                coreModule,
                appModule ,
                createModule ,
                dbModule,
                translateModule
            )
        }
    }
}

private fun Context.createWanderaNotificationChannel(){
    val channel = NotificationChannel(
        NotificationUtil.CHANNEL_ID,
        NotificationUtil.CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        enableVibration(false)
        setSound(null,null)
    }
    val manager = getSystemService<NotificationManager>()
    manager?.createNotificationChannel(channel)

}
