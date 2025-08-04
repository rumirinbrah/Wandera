package com.zzz.core.presentation.components


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Progress bar with two circles around. One outer & one inner.
 * @param componentSize Overall component size
 */
@Composable
fun DoublyProgressBar(
    color: Color = MaterialTheme.colorScheme.onBackground,
    componentSize : Dp = 100.dp ,
    rotationAngle : Float = 270f ,
) {
    val transition = rememberInfiniteTransition(label="")
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000) ,
            repeatMode = RepeatMode.Reverse
        ) ,
        label = "progress"
    )
    val rotation = transition.animateFloat(
        initialValue = 0f ,
        targetValue = rotationAngle ,
        animationSpec = infiniteRepeatable(
            tween(3000 , easing = LinearEasing) ,
            repeatMode = RepeatMode.Reverse
        ) ,
        label = "Rotation"
    )
    Canvas(
        Modifier.size(componentSize)
    ) {
        rotate(rotation.value){
            drawArc(
                //topLeft = Offset(x=size.width/2f,y=size.height/2f) ,
                style = Stroke(5f) ,
                color = color,
                startAngle = 45f ,
                sweepAngle = 360f * progress.value,
                size = Size(size.width,size.height) ,
                useCenter = false
            )
            drawArc(
                topLeft = Offset(x=size.width/4f,y=size.height/4f) ,
                style = Stroke(5f) ,
                color = color,
                startAngle = 45f ,
                sweepAngle = -360f * progress.value,
                size = Size(size.width/2f,size.height/2f) ,
                useCenter = false
            )
        }

    }
}

