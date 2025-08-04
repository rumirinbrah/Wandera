package com.zzz.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

/**
 * Creates a haze/fade effect. Can be applied at the bottom of any scrollable component.
 * @param background Background color of your composable
 * @param hazeColor Color of the fade effect
 * @param height Height of haze component
 * @author zyzz
 */
@Composable
fun HazeEffectRectangle(
    modifier: Modifier = Modifier ,
    background : Color = MaterialTheme.colorScheme.background ,
    hazeColor : Color = Color.DarkGray ,
    height : Dp = 60.dp
) {
    val blendedColor = remember {
        ColorUtils.blendARGB(hazeColor.toArgb(),background.toArgb(),0.6f)
    }
    val brush = remember {
        Brush.verticalGradient(
            colors = listOf(
                background.copy(0.2f),
                background,
                Color(blendedColor) ,
            )
        )
    }
    Canvas(
        modifier.height(height)
    ) {
        drawRect(
            brush = brush,
            alpha = 0.5f,
            //blendMode = BlendMode.Luminosity
        )
    }
}