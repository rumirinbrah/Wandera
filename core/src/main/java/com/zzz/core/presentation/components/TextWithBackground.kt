package com.zzz.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextWithBackground(
    text : String ,
    fontSize : TextUnit = 30.sp ,
    fontWeight : FontWeight = FontWeight.ExtraBold ,
    maxLines : Int = 3 ,
    lineHeight : TextUnit = 32.sp ,
    background : Color = Color.Gray,
    bgAlpha : Float = 0.4f ,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(Shapes().large)
            .background(
                background.copy(alpha = bgAlpha)
            )
            .padding(8.dp)
    ){
        Text(
            text,
            fontSize = fontSize,
            color = Color.White,
            fontWeight = fontWeight,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            lineHeight = lineHeight,
            modifier = Modifier.widthIn(max = 150.dp)
        )
    }
}