package com.zzz.feature_trip.overview.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.components.ImageComponent
import com.zzz.core.presentation.components.TextWithBackground
import com.zzz.core.theme.daySky
import com.zzz.data.trip.model.Day

@Composable
fun PagerItem(
    day: Day,
    dayTitle : String = "Untitled",
    modifier: Modifier = Modifier
) {

    Box(
        modifier
            .clip(CircleShape)
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(daySky),
        contentAlignment = Alignment.Center
    ){
        ImageComponent(
            imageUri = day.image,
            contentDescription = day.locationName,
            modifier = Modifier.fillMaxSize()
        )
        TextWithBackground(day.locationName)
    }
}