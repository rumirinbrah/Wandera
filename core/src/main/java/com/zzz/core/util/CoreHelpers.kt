package com.zzz.core.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Converts a URI to a Bitmap
 */
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
/**
 * Extracts dominant colors from a bitmap
 */
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

/**
 * Long to LocalDateTime
 */
fun Long.toLocalDateTime():LocalDateTime{
    return try {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(this) , ZoneId.systemDefault())
    }catch (e : Exception){
        e.printStackTrace()
        LocalDateTime.now()
    }
}
fun Long.toLocalDate() : LocalDate{
    return try {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(this) ,
            ZoneId.systemDefault()
        ).toLocalDate()
    }catch (e : Exception){
        e.printStackTrace()
        LocalDateTime.now().toLocalDate()
    }
}

/**
 * Returns whether the mime type is Image
 */
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
                false
            }
            else -> {
                false
            }
        }
    }
}

/**
 * Returns the type of file. For supported types, see [FileType]
 * @see FileType
 */
suspend fun Context.getMimeType(uri: Uri) : FileType{
    return withContext(Dispatchers.IO){
        val mimeType = contentResolver.getType(uri)

        try {
            return@withContext when(mimeType){
                "null"->{
                    FileType.CORRUPT
                }
                "image/jpeg"->{
                    FileType.IMAGE
                }
                "image/png"->{
                    FileType.IMAGE
                }
                "application/pdf"->{
                    FileType.PDF
                }
                else-> FileType.UNKNOWN
            }
        } catch (e : SecurityException) {
            println("here")
            e.printStackTrace()
            return@withContext FileType.CORRUPT
        }catch (e : Exception){
            e.printStackTrace()
            return@withContext FileType.CORRUPT
        }
    }
}

@ChecksSdkIntAtLeast(parameter = 0)
fun isSdkVersionGreaterThanEqualTo(sdk : Int) : Boolean{
    return Build.VERSION.SDK_INT >= sdk
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
/**
 * Share text via other apps
 */
fun Context.shareText(text : String){
    val intent = Intent(ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT,text)
    }
    val chooser = Intent.createChooser(intent,"Share expenses via")
    startActivity(chooser)

}
