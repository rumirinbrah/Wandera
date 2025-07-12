package com.zzz.core.presentation.image_picker.viewmodel

import android.net.Uri

data class GalleryImage(
    val id : Long,
    val image : Uri,
    val albumName : String ="Unknown"
)
