package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.zzz.core.presentation.components.ImageComponent
import com.zzz.core.presentation.components.TextWithBackground
import com.zzz.core.theme.daySky
import com.zzz.data.trip.model.Day

@Composable
fun PagerItem(
    day: Day,
    onClick : (Day)->Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier
            .clip(CircleShape)
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(daySky)
            .clickable {
                onClick(day)
            },
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