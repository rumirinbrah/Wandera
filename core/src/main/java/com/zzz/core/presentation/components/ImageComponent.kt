package com.zzz.core.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zzz.core.R

/**
 * @author zyzz
 */
@Composable
fun ImageComponentWithDefaultBackground(
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

@Composable
fun ImageComponent(
    imageUri : Uri? = null,
    contentDescription : String = "",
    background : Color = MaterialTheme.colorScheme.surfaceContainer,
    modifier: Modifier = Modifier,
    contentScale : ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUri)
            .crossfade(true)
            .build(),
        contentDescription = "Image - $contentDescription",
        contentScale = contentScale,
        modifier = modifier
            .background(background)
    )
}