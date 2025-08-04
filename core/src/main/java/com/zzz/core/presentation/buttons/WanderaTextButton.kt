package com.zzz.core.presentation.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WanderaTextButton(
    text : String,
    onClick : () ->Unit,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    modifier: Modifier = Modifier,
    fontSize : TextUnit = 14.sp,
    fontWeight : FontWeight = FontWeight.Normal,
    background : Color = MaterialTheme.colorScheme.surfaceContainer.copy(0.3f),
    onBackground : Color = MaterialTheme.colorScheme.onBackground,
) {
    Box(
        modifier
            .clip(MaterialTheme.shapes.large)
            .background(background)
            .clickable {
                onClick()
            }
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ){
        Text(
            text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = onBackground
        )
    }
}