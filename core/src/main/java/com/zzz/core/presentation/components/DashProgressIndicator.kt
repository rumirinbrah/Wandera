package com.zzz.core.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

@Composable
fun DashProgressIndicator(
    modifier: Modifier = Modifier,
    dashHeight : Dp = 5.dp,
    color : Color = MaterialTheme.colorScheme.primary
) {
    val transition = rememberInfiniteTransition()
    val translation = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(700 , delayMillis = 50),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier.fillMaxWidth()
            .drawBehind {
                    drawRect(
                        color = color,
                        size = Size(
                            width = lerp(start = 30f, stop = 40f,translation.value) ,
                            height = dashHeight.toPx()
                        ),
                        topLeft = Offset(x = size.width * translation.value, y =0f)
                    )
            }
    )
}