package com.zzz.core.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

suspend fun getBitmapFromUri(context: Context , uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val bitmap = context.contentResolver.openInputStream(uri)
                ?.use {
                    BitmapFactory.decodeStream(it)
                }
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
suspend fun getColorFromBitmap(bitmap: Bitmap): Color? {
    return withContext(Dispatchers.Default){
        val palette = Palette.from(bitmap).generate()
        if(palette.dominantSwatch?.rgb == null){
            null
        }else{
            Color(palette.dominantSwatch?.rgb ?: 0xFFFFFF)
        }
    }

}

//Long to Date
fun Long.toLocalDateTime():LocalDateTime{
    return try {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(this) , ZoneId.systemDefault())
    }catch (e : Exception){
        e.printStackTrace()
        LocalDateTime.now()
    }

}

suspend fun Context.isMimeTypeImg(uri: Uri):Boolean {
    return withContext(Dispatchers.IO) {

        val mimeType = contentResolver.getType(uri)

        return@withContext when (mimeType) {
            "image/jpeg" -> {
                true
            }

            "image/png" -> {
                true
            }
            "application/pdf"->{
                println("PDF")
                false
            }
            else -> {
                false
            }
        }
    }
}

//view PDF
fun Context.openPDF(uri: Uri){
    println("URI IS $uri")
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri,"application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    val chooser = Intent.createChooser(intent,"Choose an app to view PDF")
    if(chooser.resolveActivity(packageManager)!=null){
        startActivity(chooser)
    }else{
        Toast.makeText(
            this ,
            "No PDF viewer found!" ,
            Toast.LENGTH_SHORT
        ).show()
    }
}
//VIEW IMG
fun Context.openImageInGallery(uri: Uri){
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri,"image/*")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    val chooser = Intent.createChooser(intent,"Choose an app to view image")
    if(chooser.resolveActivity(packageManager)!=null){
        startActivity(chooser)
    }else{
        Toast.makeText(
            this ,
            "No Photo viewer found!" ,
            Toast.LENGTH_SHORT
        ).show()
    }
}

//send text
fun Context.shareText(text : String){
    val intent = Intent(ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT,text)
    }
    val chooser = Intent.createChooser(intent,"Share expenses via")
    startActivity(chooser)

}
