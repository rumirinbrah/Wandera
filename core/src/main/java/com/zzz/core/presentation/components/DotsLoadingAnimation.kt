package com.zzz.core.presentation.components


import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp


/**
 * @param dotSize - The size of bouncy dots
 * @param bounceSize - The vertical bounce size of animating dots
 * @param color - Dots color
 */
@Composable
fun DotsLoadingAnimation(
    dotSize : Dp = 20.dp,
    bounceSize : Int = 10,
    color: Color = MaterialTheme.colorScheme.primaryContainer ,
) {
    val transition = rememberInfiniteTransition(label = "animation")

    val dot1 = transition.animateFloat(
        initialValue = 0f ,
        targetValue = 1f ,
        animationSpec = infiniteRepeatable(
            tween(500) ,
            repeatMode = RepeatMode.Reverse
        )
    )
    val dot2 = transition.animateFloat(
        initialValue = 0f ,
        targetValue = 1f ,
        animationSpec = infiniteRepeatable(
            tween(500) ,
            repeatMode = RepeatMode.Reverse ,
            initialStartOffset = StartOffset(250)
        )
    )
    val dot3 = transition.animateFloat(
        initialValue = 0f ,
        targetValue = 1f ,
        animationSpec = infiniteRepeatable(
            tween(500) ,
            repeatMode = RepeatMode.Reverse ,
            initialStartOffset = StartOffset(500)
        )
    )

    Row (
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Box(
            Modifier.dotAnimation(dot1.value,bounceSize)
                .size(dotSize)
                .clip(CircleShape)
                .background(color)
        )
        Box(
            Modifier.dotAnimation(dot2.value,bounceSize)
                .size(dotSize)
                .clip(CircleShape)
                .background(color)
        )
        Box(
            Modifier.dotAnimation(dot3.value,bounceSize)
                .size(dotSize)
                .clip(CircleShape)
                .background(color)
        )
    }



}

/**
 * @param anchor - Infinite animation value (0..1f)
 * @param size - Size of vertical bounce
 */
private fun Modifier.dotAnimation(anchor: Float , size: Int): Modifier {

    return offset {
        IntOffset(
            0 ,
            (-2 * size * anchor.dp.toPx()).toInt()
        )
    }
        .scale(lerp(1f , 1.25f , anchor))
        .alpha(lerp(0.75f , 1f , anchor))
}