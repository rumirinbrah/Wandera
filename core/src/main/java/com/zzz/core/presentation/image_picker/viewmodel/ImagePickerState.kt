package com.zzz.core.presentation.image_picker.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LauncherImagePickerState {

    var pickerVisible by mutableStateOf(false)
        private set

    fun launch(){
        pickerVisible = true
    }
    fun dismiss(){
        pickerVisible = false
    }

}

internal data class ImagePickerState(
    val images : List<GalleryImage> = emptyList(),
    val albums : List<GalleryAlbum> = emptyList(),
    val albumImages : List<GalleryImage> = emptyList(),
    val selectedImage : Uri? = null,
    val selectedAlbum : String? = null,
    val tabRowVisible : Boolean = true,
    val loading : Boolean = false,
    val loadingAlbumImages : Boolean = false,
)