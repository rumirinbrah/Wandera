package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.R
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.appIconBackground
import com.zzz.core.theme.successGreen
import com.zzz.core.util.isSdkVersionGreaterThanEqualTo
import kotlinx.coroutines.delay


@Composable
fun MarkedAsDoneRoot(
    modifier: Modifier = Modifier,
    onAnimationFinish: () -> Unit
) {
    if(isSdkVersionGreaterThanEqualTo(30)){
        FancyMarkedAsDoneAnimation(
            onAnimationFinish = onAnimationFinish,
            modifier = modifier
        )
    }else{
        MarkedAsDoneAnimation(
            onAnimationFinish = onAnimationFinish,
            modifier = modifier
        )
    }
}
@Composable
private fun FancyMarkedAsDoneAnimation(
    onAnimationFinish: () -> Unit ,
    scalingCircleColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.8f) ,
    circleStroke: Float = 10f ,
    background : Color = MaterialTheme.colorScheme.background,
    onBackground : Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = "circle animation"
    )
    val circleTranslation = remember {
        Animatable(
            initialValue = 0f
        )
    }

    val rectVisible = remember { mutableStateOf(false) }
    val contentVisible = remember { mutableStateOf(false) }

    val circleAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if(contentVisible.value) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000 ,
                delayMillis = 200
            )
        )
    )


    LaunchedEffect(Unit) {
        delay(300)
        circleTranslation.animateTo(
            1f ,
            animationSpec = tween(
                1500,
                easing = EaseOutBounce
            )
        )
        rectVisible.value = true
        delay(1200)
        contentVisible.value = true
        delay(4500)
        onAnimationFinish()
    }

    Box(
        Modifier
            .background(background)
            .fillMaxSize()
            .drawWithCache {
                onDrawWithContent {
                    translate(
                        top = size.height / 2 * circleTranslation.value
                    ) {
                        drawCircle(
                            radius = 50.dp.toPx() ,
                            color = onBackground ,
                            center = Offset(x = size.width / 2 , y = 0f)
                        )
                    }
                    drawContent()

                }
            } ,
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            rectVisible.value,
            enter = scaleIn(
                animationSpec = tween(
                    1000,
                    easing = EaseOutExpo
                )
            )
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(appIconBackground),
                contentAlignment = Alignment.Center
            ){
                AnimatedVisibility(
                    contentVisible.value,
                    enter = fadeIn()
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier
                                .size(80.dp)
                                .drawBehind {
                                    drawCircle(
                                        color = scalingCircleColor ,
                                        style = Stroke(circleStroke) ,
                                        radius = (size.width) * circleAnimation.value ,
                                        alpha = 1f - (circleAnimation.value) ,
                                    )
                                } ,
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.download_done) ,
                                contentDescription = "Tick icon" ,
                                tint = Color.White ,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        VerticalSpace()
                        Text(
                            "\" Every trip comes to an end, but a part of you never comes back the same \"" ,
                            textAlign = TextAlign.Center ,
                            fontSize = 15.sp ,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                    }


            }
        }


    }
}

@Composable
private fun MarkedAsDoneAnimation(
    onAnimationFinish: () -> Unit ,
    scalingCircleColor : Color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
    circleStroke : Float = 10f,
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = "circle animation"
    )
    val circleAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000 ,
                delayMillis = 200
            )
        )
    )

    LaunchedEffect(Unit) {
        delay(4500)
        onAnimationFinish()
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(80.dp)
                    .drawBehind {
                        drawCircle(
                            color = scalingCircleColor ,
                            style = Stroke(circleStroke) ,
                            radius = (size.width) * circleAnimation.value ,
                            alpha = 1f - (circleAnimation.value) ,
                        )
                    },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.download_done),
                    contentDescription = "Tick icon",
                    tint = successGreen,
                    modifier = Modifier.size(60.dp)
                )
            }
            VerticalSpace()
            Text(
                "\" Every trip comes to an end, but a part of you never comes back the same \"",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun FinishPrev() {
    MaterialTheme {
        MarkedAsDoneAnimation(
            onAnimationFinish = {}
        )
    }
}