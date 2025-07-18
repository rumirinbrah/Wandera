package com.zzz.feature_trip.create.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun IndicatorCard(
    text : String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth()
            .height(100.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
            .alpha(0.5f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
    }
}
@Composable
fun IndicatorCard(
    text : AnnotatedString,
    background : Color = MaterialTheme.colorScheme.surfaceContainer,
    onBackground : Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentAlpha : Float = 0.5f,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth()
            .height(100.dp)
            .clip(MaterialTheme.shapes.large)
            .background(background)
            .padding(16.dp)
            .alpha(contentAlpha),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = onBackground)
    }
}
@Composable
fun IndicatorCard(
    text : String ,
    @DrawableRes image : Int ,
    background : Color = MaterialTheme.colorScheme.surfaceContainer ,
    onBackground : Color = MaterialTheme.colorScheme.onSurfaceVariant ,
    contentAlpha : Float = 0.5f ,
    modifier: Modifier = Modifier
) {
    Column (
        modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .border(1.dp,background,MaterialTheme.shapes.large)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = text,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
        Text(text, color = onBackground)
    }
}