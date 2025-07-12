package com.zzz.core.presentation.theme_change

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzz.core.R
import com.zzz.core.theme.WanderaTheme
import com.zzz.core.theme.daySky
import com.zzz.core.theme.nightSky
import kotlinx.coroutines.launch

@Composable
fun ChangeThemePage(
    modifier: Modifier = Modifier ,
    night: Boolean = true ,
    toggleDarkMode: (darkMode: Boolean) -> Unit ,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val backgroundTopColor = remember {
        Animatable(
            initialValue = if (night) {
                Color.Black
            } else {
                Color(0xFF71ACD7)
            }
        )
    }


    var containerWidth by remember {
        mutableFloatStateOf(0f)
    }
    var buttonHandleWidth by remember {
        mutableFloatStateOf(0f)
    }
    val buttonHandlePosition = remember(containerWidth) {
        androidx.compose.animation.core.Animatable(
            initialValue = if (night) 0f else containerWidth - buttonHandleWidth
        )
    }

    val buttonBackgroundPainter = painterResource(R.drawable.stars_cloud_bg)



    //var night by remember { mutableStateOf(true) }
    val buttonIcon by remember(night) {
        mutableIntStateOf(
            if (night) {
                R.drawable.moon_theme
            } else {
                R.drawable.sun_theme
            }
        )
    }
    //to animate states
    val animationFraction = animateFloatAsState(
        targetValue = if (night) 0f else 1f ,
        animationSpec = tween(1000)
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        backgroundTopColor.value ,
                        Color(0xFF56ACE5)
                    )
                )
            )
            .padding(innerPadding)
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .width(80.dp)
                .height(40.dp)
                .graphicsLayer {
                    containerWidth = size.width
                }
                .clip(RoundedCornerShape(40.dp))
                .background(
                    if (night) {
                        nightSky
                    } else {
                        daySky
                    }
                )
                .clickable {
                    scope.launch {
                        if (night) {
                            launch {
                                buttonHandlePosition.animateTo(
                                    containerWidth - buttonHandleWidth ,
                                    tween(1000)
                                )
                            }
                            launch {
                                backgroundTopColor.animateTo(
                                    Color(0xFF71ACD7) ,
                                    tween(1000)
                                )
                            }
                            toggleDarkMode(false)

                        } else {
                            launch {
                                buttonHandlePosition.animateTo(
                                    0f ,
                                    tween(1000)
                                )
                            }
                            launch {
                                backgroundTopColor.animateTo(
                                    Color.Black ,
                                    tween(1000)
                                )
                            }
                            toggleDarkMode(true)

                        }
                    }
                }
                .align(Alignment.TopCenter) ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Crossfade(
                targetState = buttonIcon ,
                animationSpec = spring()
            ) { icon ->
                Image(
                    painter = painterResource(
                        icon
                    ) ,
                    contentDescription = null ,
                    modifier = Modifier
                        .size(35.dp)
                        .graphicsLayer {
                            buttonHandleWidth = size.width
                            translationX = buttonHandlePosition.value
                        }
                )
            }


        }

        Canvas(
            Modifier
                .padding(8.dp)
                .width(80.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(40.dp))
                .align(Alignment.TopCenter)
        ) {
            with(buttonBackgroundPainter) {
                val scale = size.width / intrinsicSize.width
                val scaledHeight = intrinsicSize.height * scale
                translate(
                    top = (size.height - scaledHeight) * (animationFraction.value)
                ) {
                    draw(
                        Size(size.width , scaledHeight)
                    )
                }

            }
        }
        Image(
            painter = painterResource(R.drawable.mountain_bg) ,
            contentDescription = null ,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter) ,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun ThemePrev() {
    MaterialTheme {
        //ChangeThemePage()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        var night by remember { mutableStateOf(true) }

        var containerWidth = remember { 0f }
        var iconWidth = remember { 0f }
        val buttonIcon by remember(night) {
            mutableIntStateOf(
                if (night) {
                    R.drawable.moon_theme
                } else {
                    R.drawable.sun_theme
                }
            )
        }

        val handlePosition = remember {
            androidx.compose.animation.core.Animatable(
                initialValue = 0f
            )
        }
        val backgroundTopColor = animateColorAsState(
            targetValue = if(night) Color.Black else Color(0xFF71ACD7),
            animationSpec = tween(1000)
        )
        //to animate states
        val animationFraction = animateFloatAsState(
            targetValue = if (night) 0f else 1f ,
            animationSpec = tween(1000)
        )
        val buttonBackgroundPainter = painterResource(R.drawable.stars_cloud_bg)


        Box(
            Modifier.fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            backgroundTopColor.value ,
                            Color(0xFF56ACE5)
                        )
                    )
                )
        ) {
            Row(
                Modifier
                    .padding(8.dp)
                    .width(80.dp)
                    .height(40.dp)
                    .graphicsLayer {
                        containerWidth = size.width
                    }
                    .clip(RoundedCornerShape(40.dp))
                    .background(
                        if (night) {
                            nightSky
                        } else {
                            daySky
                        }
                    )
                    .clickable {
                        when {
                            !night -> {
                                scope.launch {
                                    handlePosition.animateTo(
                                        0f,
                                        tween(1000)
                                    )
                                }
                            }

                            night -> {
                                scope.launch {
                                    handlePosition.animateTo(
                                        containerWidth - iconWidth,
                                        tween(1000)
                                    )
                                }
                            }
                        }
                        night = !night
                    }
                    .align(Alignment.TopCenter) ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Crossfade(
                    targetState = buttonIcon ,
                    animationSpec = spring()
                ) { icon ->
                    Image(
                        painter = painterResource(
                            icon
                        ) ,
                        contentDescription = null ,
                        modifier = Modifier
                            .size(35.dp)
                            .graphicsLayer {
                                iconWidth = size.width
                                translationX = handlePosition.value
                            }
                    )
                }
            }
            Canvas(
                Modifier
                    .padding(8.dp)
                    .width(80.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .align(Alignment.TopCenter)
            ) {
                with(buttonBackgroundPainter) {
                    val scale = size.width / intrinsicSize.width
                    val scaledHeight = intrinsicSize.height * scale
                    translate(
                        top = (size.height - scaledHeight) * (animationFraction.value)
                    ) {
                        draw(
                            Size(size.width , scaledHeight)
                        )
                    }

                }
            }

        }

    }
}