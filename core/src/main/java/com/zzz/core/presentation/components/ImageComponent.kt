package com.zzz.core.presentation.components

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zzz.core.R

@Composable
fun ImageComponent(
    title : String,
    imageUri : Uri? = null,
    modifier: Modifier = Modifier,
    contentScale : ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUri ?: R.drawable.test_trees)//
            .crossfade(true)
            .build(),
        contentDescription = "Image - $title",
        contentScale = contentScale,
        modifier = modifier
    )
}