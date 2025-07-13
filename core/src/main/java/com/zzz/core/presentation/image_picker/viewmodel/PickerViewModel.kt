package com.zzz.core.presentation.image_picker.viewmodel

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class PickerViewModel(
    private val context: Context
) : ViewModel() {

    private val loggingEnabled = true

    private val _state = MutableStateFlow(ImagePickerState())
    val state = _state.asStateFlow()

    private val allRecentImages = mutableListOf<GalleryImage>()

    private var currentPage = 0
    private val pageSize = 20
    private var endReached = false


    init {

        log {
            "init..."
        }
    }

    fun onAction(action: ImagePickerActions) {
        when (action) {
            ImagePickerActions.CancelSelection -> {
                clearViewModel()
            }

            ImagePickerActions.Load -> {
                loadRecentImages()
            }
            ImagePickerActions.LoadRecentsNextPage->{
                loadRecentsNextPage()
            }

            is ImagePickerActions.LoadAlbumImages -> {
                loadAlbumImages(action.albumName)
            }

            ImagePickerActions.ClearAlbumImages -> {
                clearAlbumImages()
            }

            is ImagePickerActions.SelectImage -> {

            }
        }
    }

    //all recent images
    private fun loadRecentImages() {
        if (allRecentImages.isNotEmpty()) {
            log {
                "Already loaded recents, returning..."
            }
            return
        }
        log {
            "Loading images..."
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                try {
                    val projection = arrayOf(
                        MediaStore.Images.Media._ID ,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME ,
                    )
                    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
                    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    ensureActive()
                    context.contentResolver.query(
                        uri ,
                        projection ,
                        null ,
                        null ,
                        sortOrder
                    )?.use { cursor ->
                        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        val albumCol =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                        while (cursor.moveToNext()) {
                            ensureActive()
                            val id = cursor.getLong(idCol)
                            val albumName = cursor.getString(albumCol) ?: "Unknown"
                            val contentUri = ContentUris.withAppendedId(uri , id)
                            allRecentImages.add(
                                GalleryImage(id , contentUri , albumName)
                            )
                        }

                    }
                } catch (e: Exception) {
                    log {
                        "loadCameraImages : Error"
                    }
                    e.printStackTrace()
                    ensureActive()
                }
            }
            log {
                "Recents loaded!!!"
            }
            loadRecentsNextPage()
            readyAlbums()

        }

    }

    //paging
    private fun loadRecentsNextPage() {
        if (endReached) {
            log {
                "End reached for recents"
            }
            return
        }
        if (_state.value.loading) {
            log {
                "Already loading..."
            }
            return
        }
        viewModelScope.launch {
            val start = currentPage * pageSize
            if (start > allRecentImages.size) {
                log {
                    "End reached for recents"
                }
                endReached = true
                return@launch
            }
            val end = (start + pageSize).coerceAtMost(allRecentImages.size)
            _state.update {
                it.copy(loading = true)
            }
            log {
                "Recents ; Loading more..."
            }

            val nextPage = allRecentImages.subList(start , end)
            _state.update {
                it.copy(
                    images = it.images + nextPage ,
                    loading = false
                )
            }

            currentPage += 1
            log {
                "Current page is $currentPage"
            }

        }
    }

    private fun readyAlbums() {
        when {
            allRecentImages.isEmpty() -> {
                log {
                    "No recent images for making albums"
                }
                return
            }

            _state.value.albums.isNotEmpty() -> {
                log {
                    "Albums already fetched!"
                }
                return
            }
        }

        viewModelScope.launch {
            val tempAlbums = allRecentImages.groupBy {
                it.albumName
            }.map { (album , images) ->

                val coverImage = images.maxByOrNull { image ->
                    image.id
                } ?: images.first()

                GalleryAlbum(
                    albumName = album ,
                    coverImage = coverImage
                )
            }

            _state.update {
                it.copy(albums = tempAlbums)
            }
            log {
                "Albums mapped!!"
            }
        }
    }

    private fun loadAlbumImages(albumName: String) {
        if (albumName.isBlank()) {
            return
        }
        log {
            "Loading album images...2"
        }
        viewModelScope.launch {
            _state.update {
                it.copy(loadingAlbumImages = true , selectedAlbum = albumName)
            }

            val images = allRecentImages.filter {
                it.albumName == albumName
            }

            _state.update {
                it.copy(
                    loadingAlbumImages = false ,
                    albumImages = images
                )
            }

        }
    }

    private fun clearAlbumImages() {
        viewModelScope.launch {
            log {
                "Clearing album images"
            }
            _state.update {
                it.copy(
                    loadingAlbumImages = false ,
                    albumImages = emptyList() ,
                    selectedAlbum = "Unknown"
                )
            }
        }
    }


    private fun log(msg: () -> String) {
        if (loggingEnabled) {
            Log.d("pickerVm" , msg())
        }
    }
    fun clearViewModel(){
        //onCleared()
        log {
            "Clearing picker vm..."
        }
        allRecentImages.clear()
        currentPage = 0
        endReached = false
        _state.update {
            ImagePickerState(
                selectedImage = it.selectedImage
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        log {
            "onCleared : Clearing picker vm..."
        }
    }

}