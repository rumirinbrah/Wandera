package com.zzz.feature_translate.presentation.tab_download.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun FilterButton(
    text : String,
    filtered : Boolean = false,
    onClick : (filtered : Boolean)->Unit,
    modifier: Modifier = Modifier
) {
    val background = animateColorAsState(
        targetValue = if(filtered){
            MaterialTheme.colorScheme.primary
        }else{
            MaterialTheme.colorScheme.surfaceContainer

        }
    )

    val textColor = animateColorAsState(
        targetValue = if(filtered){
            MaterialTheme.colorScheme.onPrimary
        }else{
            MaterialTheme.colorScheme.onBackground

        }
    )

    Box(
        modifier.clip(MaterialTheme.shapes.small)
            .background(
                background.value
            )
            .clickable {
                onClick(!filtered)
            }
            .padding(vertical = 5.dp, horizontal = 18.dp) ,
        contentAlignment = Alignment.Center
    ){
        Text(
            text,
            color = textColor.value
        )
    }
}