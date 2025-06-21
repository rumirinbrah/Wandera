package com.zzz.core.platform

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.zzz.core.R

/**
 * Notification manager
 */
class WanderaNotification (
    private val context : Context
){
    private var notificationManager  = context.getSystemService<NotificationManager>()
    private val loggingEnabled : Boolean = true

    fun sendDownloadNotification(modelName : String){

        log {
            "Sending download notification for $modelName"
        }
        val builder = NotificationCompat
            .Builder(context , NotificationUtil.CHANNEL_ID)
            .setSmallIcon(R.drawable.download)
            .setContentTitle("Wandera")
            .setContentText("Downloading $modelName translation model...")
            .setSilent(true)
            .setProgress(1,1,true)
            .build()

        notificationManager?.notify(2,builder)


    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("wanderaNoti" , msg())
        }
    }

}