package com.zzz.core.presentation.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NormalButton(
    title : String,
    onClick :()->Unit,
    enabled : Boolean = false,
    fontSize : TextUnit = 15.sp,
    horizontalPadding : Dp = 16.dp,
    verticalPadding : Dp = 8.dp,
    background : Color = MaterialTheme.colorScheme.primary,
    onBackground : Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(Shapes().large)
            .background(background)
            .clickable {
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