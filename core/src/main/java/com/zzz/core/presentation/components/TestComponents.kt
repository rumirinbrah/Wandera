package com.zzz.core.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Components to be used for testing
 */
@Composable
fun TestLazyColumn(
    modifier: Modifier = Modifier,
    items : Int = 50,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val translation = animateFloatAsState(
        targetValue = if(isPressed.value){
            60f
        }else{
            0f
        },
        animationSpec = tween(500)
    )
    val infiniteTransition = rememberInfiniteTransition()
    val shake = infiniteTransition.animateFloat(
        targetValue = 20f,
        initialValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(200),
            repeatMode = RepeatMode.Reverse
        )
    )


    Column(
        modifier.fillMaxSize()
    ) {
        Box(
            Modifier.clip(CircleShape)
                .fillMaxWidth()
                .clickable(
                    onClick = {

                    },
                    interactionSource = interactionSource,
                    indication = null
                )
                .padding(8.dp)
                .graphicsLayer {
                    translationX = translation.value
                    translationY = shake.value
                }
        ){
            Icon(
                imageVector = Icons.Default.Settings,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null
            )
        }
        LazyColumn(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items){
                Text(
                    "HEEEEEE" ,
                    fontSize = 25.sp ,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}