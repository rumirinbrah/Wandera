package com.zzz.core.presentation.buttons

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author zyzz
 */
@Composable
fun NormalButton(
    title : String,
    onClick :()->Unit,
    contentDescription : String? = null,
    enabled : Boolean = true,
    fontSize : TextUnit = 15.sp,
    horizontalPadding : Dp = 16.dp,
    verticalPadding : Dp = 8.dp,
    shape: Shape = MaterialTheme.shapes.large,
    background : Color = MaterialTheme.colorScheme.primary,
    onBackground : Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .widthIn(100.dp,150.dp)
            .clip(shape)
            .background(background)
            .clickable(
                onClickLabel = contentDescription
            ) {
                if(enabled){
                    onClick()
                }
            }
            .padding(horizontal=horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ){
        Text(
            title,
            color = onBackground,
            fontSize = fontSize
        )
    }
}

/**
 * Button with custom shrink animation when pressed.
 * @param scaleValue Amount by which button will be scaled on shrink
 * @param animationSpec Animation spec for shrink animation
 * @author zyzz
 */
@Composable
fun NormalButton(
    title : String,
    onClick :()->Unit,
    contentDescription : String? = null,
    animationSpec : AnimationSpec<Float> = tween<Float>(200),
    scaleValue : Float = 0.95f,
    enabled : Boolean = true,
    fontSize : TextUnit = 15.sp,
    horizontalPadding : Dp = 16.dp,
    verticalPadding : Dp = 8.dp,
    shape: Shape = MaterialTheme.shapes.large,
    background : Color = MaterialTheme.colorScheme.primary,
    onBackground : Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale = animateFloatAsState(
        targetValue = if(isPressed) scaleValue else 1f,
        animationSpec = animationSpec
    )


    Box(
        modifier
            .scale(1f * scale.value)
            .clip(shape)
            .widthIn(100.dp,150.dp)
            .background(background)
            .clickable(
                onClickLabel = contentDescription,
                onClick = {
                    if(enabled){
                        onClick()
                    }
                },
                indication = null,
                interactionSource = interactionSource
            )
            .padding(horizontal=horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ){
        Text(
            title,
            color = onBackground,
            fontSize = fontSize
        )
    }
}

