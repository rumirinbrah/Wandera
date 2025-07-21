package com.zzz.wandera.presentation.intro

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.modifiers.baldyShape
import com.zzz.core.theme.appIconBackground
import com.zzz.wandera.R
import kotlinx.coroutines.delay

/**
 * App first time screens
 */
@Composable
fun WanderaIntro(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(initialPage = 2) { 3 }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFADDCD7) ,
            Color(0xFF70C4BC)
        )
    )
    //Color(0xFF01675D)

    val parentContainerHeight = remember { mutableFloatStateOf(0f) }
    val parentContainerWidth = remember { mutableFloatStateOf(0f) }


    Box(
        Modifier
            .fillMaxSize()
            .background(
                //brush = backgroundBrush
                Color(0xFF01675D)
            )
            .onGloballyPositioned {
                parentContainerHeight.floatValue = it.size.height.toFloat()
                parentContainerWidth.floatValue = it.size.width.toFloat()

            }
    ) {

        HorizontalPager(
            state = pagerState ,
            modifier = modifier
        ) { page ->
            when (page) {
                0 -> {
                    IntroPage1(
                        parentContainerWidth = parentContainerWidth.floatValue
                    )
                }

                1 -> {
                    IntroPage2(
                        parentContainerWidth = parentContainerWidth.floatValue
                    )
                }
                2->{
                    IntroPage3(

                    )
                }
            }
        }


    }

}

/**
 * Opening page
 */
@Composable
fun IntroPage1(
    parentContainerWidth: Float ,
    modifier: Modifier = Modifier
) {

    val density = LocalDensity.current

    val titleAlpha = remember {
        Animatable(0f)
    }
    val subtitleAlpha = remember {
        Animatable(0f)
    }
    val bodyContentAlpha = remember {
        Animatable(0f)
    }

    val yTranslationTitle = remember() {
        Animatable(
            initialValue = with(density) { 200.dp.toPx() }
        )
    }
    val xTranslationIcon = remember() {
        Animatable(-200f)
    }

    /*
    val infiniteTransition = rememberInfiniteTransition(
        label = "goofy box"
    )
    val startGoofyAnimation = remember { mutableStateOf(false) }
    val goofyProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(
                3000
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val goofyRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(
                2000
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

     */

    LaunchedEffect(parentContainerWidth) {
        if (parentContainerWidth < 5f) {
            return@LaunchedEffect
        }
        delay(500)
        titleAlpha.animateTo(
            1f ,
            animationSpec = tween(1000)
        )
        yTranslationTitle.animateTo(
            0f ,
            animationSpec = tween(
                durationMillis = 500 ,
                delayMillis = 200 ,
                easing = EaseInOutBack//EaseInOutQuart
            )
        )
        xTranslationIcon.animateTo(
            parentContainerWidth ,
            animationSpec = tween(2000)
        )
        subtitleAlpha.animateTo(
            1f ,
            tween(500)
        )
        bodyContentAlpha.animateTo(
            1f ,
            animationSpec = tween(1000)
        )
    }
    Column(
        modifier.fillMaxSize()
    ) {
        Spacer(Modifier.fillMaxHeight(0.2f))
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .graphicsLayer {
                    translationY = yTranslationTitle.value
                }
        ) {
            Text(
                "Wandera" ,
                fontSize = 25.sp ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier
                    .alpha(alpha = titleAlpha.value) ,
                color = Color.White ,
            )
        }
        VerticalSpace(10.dp)
        Box(
            Modifier.fillMaxWidth()

        ) {
            Icon(
                painter = painterResource(
                    R.drawable.bus
                ) ,
                tint = Color.White ,
                contentDescription = null ,
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer {
                        translationX = xTranslationIcon.value
                    }
            )
            Text(
                "Your Travel\nCompanion" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier
                    .alpha(alpha = subtitleAlpha.value)
                    .align(Alignment.Center) ,
                color = Color.White ,
                letterSpacing = 1.sp ,
                textAlign = TextAlign.Center
            )
        }

        VerticalSpace(30.dp)
        Column(
            Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(bodyContentAlpha.value) ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Here's a quick overview of the app"
            )

            CommonButton(
                text = "Get Started" ,
                onClick = {

                } ,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            CommonButton(
                text = "Skip" ,
                onClick = {

                } ,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) ,
                background = Color.DarkGray.copy(0.4f)
            )

        }

    }
//        //if(startGoofyAnimation.value){
//            Canvas(
//                Modifier.fillMaxWidth()
//                    .align(Alignment.CenterStart)
//            ) {
//                rotate(goofyRotation.value){
//                    drawRect(
//                        color = Color.Gray.copy(0.5f),
//                        size = Size(100f,100f),
//                        topLeft = Offset.Zero.copy(x = size.width*goofyProgress.value)
//                    )
//                }
//            }
//        //}

}

@Composable
fun IntroPage2(
    parentContainerWidth: Float ,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val titleAlpha = remember {
        Animatable(0f)
    }
    val contentAlpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        titleAlpha.animateTo(
            1f,
            tween(1000)
        )
        contentAlpha.animateTo(
            1f,
            tween(1100)
        )
    }

    Column(
        modifier.fillMaxSize()
    ) {
        Spacer(Modifier.fillMaxHeight(0.1f))

        Text(
            "Create travel plans" ,
            fontSize = 25.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(titleAlpha.value),
            color = Color.White ,
        )
        Spacer(Modifier.fillMaxHeight(0.1f))
        Column(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    val baldyShape = baldyShape().createOutline(size , layoutDirection , density)
                    drawOutline(
                        baldyShape ,
                        Color.White
                    )
                }
                .alpha(contentAlpha.value) ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "-Craft itinerary" ,
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier ,
                color = Color(0xFF104B44) ,
            )
            Text(
                "-Manage your documents" ,
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier ,
                color = Color(0xFF104B44) ,
            )
            Text(
                "-Create checklists & much more!" ,
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier ,
                color = Color(0xFF104B44) ,
                textAlign = TextAlign.Center
            )

            CommonButton(
                text = "Next",
                onClick = {

                },
                background = appIconBackground.copy(0.2f),
                onBackground = Color.Black
            )


        }
    }
}

@Composable
fun IntroPage3(
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize()
    ) {
        Spacer(Modifier.fillMaxHeight(0.1f))

        Text(
            "No Network?\nNo Worries!" ,
            fontSize = 25.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                //.alpha(titleAlpha.value)
            ,
            color = Color.White ,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
        Spacer(Modifier.fillMaxHeight(0.1f))
        Text(
            "With Wandera, you can perform translation offline!" ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            //.alpha(titleAlpha.value)
            ,
            color = Color.White ,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(Modifier.fillMaxHeight(0.1f))
        Text(
            "Also, you're in charge of everything" ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            //.alpha(titleAlpha.value)
            ,
            color = Color.White ,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Text(
            "-Download models" ,
            fontSize = 18.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            //.alpha(titleAlpha.value)
            ,
            color = Color.White ,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
        Text(
            "-Manage models" ,
            fontSize = 18.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            //.alpha(titleAlpha.value)
            ,
            color = Color.White ,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
private fun CommonButton(
    text: String ,
    onClick: () -> Unit ,
    modifier: Modifier = Modifier ,
    background: Color = Color.Gray.copy(0.2f) ,
    onBackground: Color = Color.White ,
) {
    Box(
        modifier
            .clip(MaterialTheme.shapes.large)
            .background(background)
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp , horizontal = 16.dp)
    ) {
        Text(text , color = onBackground)
    }
}