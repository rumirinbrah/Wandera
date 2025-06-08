package com.zzz.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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