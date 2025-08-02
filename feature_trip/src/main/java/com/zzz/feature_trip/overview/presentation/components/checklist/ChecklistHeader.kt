package com.zzz.feature_trip.overview.presentation.components.checklist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
internal fun ChecklistHeader(
    collapsed : Boolean = false,
    onCollapse : ()->Unit,
    modifier: Modifier = Modifier
) {
    val rotation = animateFloatAsState(
        targetValue = if(collapsed) 0f else 180f,
        animationSpec = tween(500)
    )

    Row (
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Don't forget your stuff!!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        IconButton(
            onClick = {
                onCollapse()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = if(collapsed) {
                    "Show checklist"
                }else{
                    "hide checklist"
                },
                modifier = Modifier.rotate(rotation.value)
            )
        }


    }
}