package com.zzz.core.presentation.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Custom toast with some goofy animations and sweep colors
 * @param state WanderaToastState that triggers the toast
 * @param colorSweepAnimationDuration Duration of the horizontal color sweep
 */
@Composable
fun WanderaToast(
    state: WanderaToastState ,
    modifier: Modifier = Modifier ,
    toastDuration : Long = 2000,
    colorSweepAnimationDuration: Int = 500 ,
    translationXOffset : Float = -15f,
    translationYOffset : Float = -10f,
) {
    val progress = remember {
        Animatable(0f)
    }


    val infiniteTransition = rememberInfiniteTransition()
    val goofyTranslationX = infiniteTransition.animateFloat(
        initialValue = -1f ,
        targetValue = 1f ,
        animationSpec = infiniteRepeatable(
            tween(1000) ,
            repeatMode = RepeatMode.Reverse
        )
    )
    val goofyTranslationY = infiniteTransition.animateFloat(
        initialValue = -1f ,
        targetValue = 1f ,
        animationSpec = infiniteRepeatable(
            tween(500) ,
            repeatMode = RepeatMode.Reverse
        )
    )

    val mutex = remember { Mutex() }

    LaunchedEffect(state.visible) {
        if (state.visible) {
            mutex.withLock {
                progress.animateTo(
                    1f ,
                    animationSpec = tween(colorSweepAnimationDuration) ,
                )
                delay(toastDuration)

                state.dismiss()
                progress.snapTo(0f)
            }
        }
    }


    AnimatedVisibility(
        state.visible ,
        enter = scaleIn() ,
        exit = scaleOut() ,
        modifier = modifier
            .padding(bottom = 10.dp)
    ) {
        Box(
            Modifier
                .graphicsLayer {
                    translationX = translationXOffset * goofyTranslationX.value
                    translationY = translationYOffset * goofyTranslationY.value
                }
                .clip(MaterialTheme.shapes.medium)
                .widthIn(max = 300.dp)
                .background(Color.Black)
                .drawBehind {
                    drawRect(
                        state.sweepColor ,
                        size = Size(width = size.width * progress.value , height = size.height) ,
                        blendMode = BlendMode.Screen
                    )
                }
                .padding(horizontal = 12.dp , vertical = 8.dp)

        ) {

            Text(
                state.message ,
                color = Color.White ,
                fontSize = 14.sp ,
                maxLines = 2 ,
                overflow = TextOverflow.Ellipsis
            )

        }
    }


}

@Composable
fun rememberWanderaToastState(): WanderaToastState {
    return remember {
        WanderaToastState()
    }
}
