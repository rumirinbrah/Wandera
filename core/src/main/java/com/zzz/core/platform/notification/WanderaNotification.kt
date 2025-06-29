package com.zzz.core.platform.notification

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.zzz.core.R
import kotlin.random.Random

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
            .setContentTitle("Download in progress")
            .setContentText("Downloading $modelName translation model...")
            .setSilent(true)
            .setProgress(1,1,true)
            .build()

        notificationManager?.notify(NotificationUtil.NOTIFICATION_ID,builder)


    }
    fun sendDownloadFinishedNotification(modelName: String){
        log {
            "Sending downloaded notification for $modelName"
        }
        val builder = NotificationCompat
            .Builder(context , NotificationUtil.CHANNEL_ID)
            .setSmallIcon(R.drawable.download)
            .setContentTitle("Wandera")
            .setContentText("$modelName translation model has been downloaded!")
            .setSilent(true)
            .build()

        notificationManager?.notify(Random.nextInt(),builder)
    }
    fun cancelDownloadingNotification(){
        log {
            "Cancelling downloading notification"
        }
        notificationManager?.cancel(NotificationUtil.NOTIFICATION_ID)
    }
    fun cancelAll(){
        log {
            "Cancelling ALL"
        }
        notificationManager?.cancelAll()
    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("wanderaNoti" , msg())
        }
    }

}