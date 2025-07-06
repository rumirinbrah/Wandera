package com.zzz.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxCircular(
    checked: Boolean,
    onCheck :(checked : Boolean)->Unit,
    modifier: Modifier = Modifier,
    iconSize : Dp = 25.dp,
    background : Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onBackground : Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    Box(
        modifier.clip(CircleShape)
            .size(iconSize)
            .background(background)
            .clickable (
                onClick = {onCheck(!checked)},
                indication = null,
                interactionSource = null
            )
    ){
        if(checked){
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Checklist item checked",
                modifier = Modifier.size(iconSize),
                tint = onBackground
            )
        }
    }
}