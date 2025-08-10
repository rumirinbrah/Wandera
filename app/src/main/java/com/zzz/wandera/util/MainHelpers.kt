package com.zzz.wandera.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity

/**
 * Checks if a JSON intent was received from other apps.
 *
 * Usually used while importing trips
 * @param onJsonReceived Callback to get the received JSON
 */
fun ComponentActivity.checkJsonIntent(onJsonReceived: (Uri) -> Unit) {
    try {
        if (intent != null && intent.action == Intent.ACTION_SEND) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val jsonUri =
                    intent.getParcelableExtra(Intent.EXTRA_STREAM , Uri::class.java)
                jsonUri?.let {
                    onJsonReceived(jsonUri)
                }
            } else {
                val jsonUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
                jsonUri?.let {
                    onJsonReceived(jsonUri)
                }
            }
        } else {
            return
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return
    }

}