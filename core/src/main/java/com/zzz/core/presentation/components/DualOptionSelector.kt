package com.zzz.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zzz.core.domain.DailyTask
import kotlinx.coroutines.launch

@Composable
fun DualOptionSelector(
    background : Color,
    selectedColor : Color,
    onClick : (DailyTask)->Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val topLeft = remember {
        Animatable(initialValue = 0f)
    }
    var canvasWidth by remember { mutableFloatStateOf(0f) }

    Row(
        modifier.fillMaxWidth()
            .background(background, RoundedCornerShape(25f))
            .drawBehind {
                canvasWidth = size.width
                drawRoundRect(
                    color = selectedColor ,
                    cornerRadius = CornerRadius(25f , 25f) ,
                    topLeft = Offset(y=0f,x=topLeft.value) ,
                    size = Size(width = size.width/2, height =size.height )
                )
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        scope.launch {
                            topLeft.animateTo(0f)
                        }
                        onClick(DailyTask.TODO)
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                "TODO" ,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Box(
            Modifier.weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        scope.launch {
                            topLeft.animateTo(canvasWidth/2)
                        }
                        onClick(DailyTask.LOCATION)
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                "Location" ,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}