package com.zzz.core.presentation.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.modifiers.drawTopCurve

/**
 * Horizontal progress indicator with animation.
 * @param progress Progress of the bar. Will be coerced in 0f..1f
 * @param animationSpec Animation spec for the transition from one progress value to another.
 * @param color Progress bar color.
 * @param trailColor Color of the line that'll drawn behind the actual progress bar.
 * @param thickness Stroke/height of the bar.
 */
@Composable
fun LineProgressBar(
    modifier: Modifier = Modifier,
    progress : Float = 1f,
    animationSpec : AnimationSpec<Float> = tween(500),
    color : Color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
    trailColor : Color = MaterialTheme.colorScheme.surfaceContainer,
    thickness : Dp = 4.dp,
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress.coerceIn(0f,1f),
        animationSpec = animationSpec
    )

    Canvas(
        modifier
            .height(thickness)
    ) {
        drawLine(
            color = trailColor,
            start = Offset(x = 0f , y = 0f) ,
            end = Offset(x= size.width , y = 0f) ,
            strokeWidth = thickness.toPx()
        )
        drawLine(
            color = color,
            start = Offset(x = 0f , y = 0f) ,
            end = Offset(
                x = size.width * animatedProgress.value ,
                y = 0f
            ) ,
            strokeWidth = thickness.toPx()
        )
    }
}

@ExperimentalComposeApi
@Composable
fun CurveProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    animationSpec : AnimationSpec<Float> = tween(500),
    color : Color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
    trailColor : Color = MaterialTheme.colorScheme.surfaceContainer,
    thickness : Dp = 4.dp,
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = animationSpec
    )

    Canvas(
        modifier
    ) {
        drawTopCurve(
            color = trailColor
        )
        drawTopCurve(
            color = color,
            progress = animatedProgress.value
        )
    }
}

