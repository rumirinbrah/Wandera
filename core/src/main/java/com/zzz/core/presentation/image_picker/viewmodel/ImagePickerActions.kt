package com.zzz.core.presentation.image_picker.viewmodel

import android.net.Uri

sealed class ImagePickerActions {
    data object Load : ImagePickerActions()
    data object LoadRecentsNextPage : ImagePickerActions()

    data class SelectImage(val image : Uri) : ImagePickerActions()
    data object CancelSelection : ImagePickerActions()

    data class LoadAlbumImages(val albumName : String) : ImagePickerActions()
    data object ClearAlbumImages : ImagePickerActions()

}