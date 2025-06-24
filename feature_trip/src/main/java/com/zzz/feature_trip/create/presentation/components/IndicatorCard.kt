package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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