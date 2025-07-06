package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.CheckboxCircular
import com.zzz.core.presentation.modifiers.drawStrikethroughLine
import com.zzz.core.presentation.modifiers.trapeziumShape
import com.zzz.core.theme.successGreen
import com.zzz.data.note.model.ChecklistEntity

/**
 * Represents a checklist entity item in a lazy col
 */
@Composable
fun ChecklistItem(
    item : ChecklistEntity ,
    onCheck : (itemId : Long , checked : Boolean)->Unit ,
    onDelete : (itemId : Long)->Unit ,
    modifier: Modifier = Modifier ,
    background : Color = MaterialTheme.colorScheme.surfaceContainer ,
    onBackground : Color = MaterialTheme.colorScheme.onSurfaceVariant ,
    strikeThroughColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val density = LocalDensity.current
    val lineProgress = remember {
        Animatable(if(item.isChecked)1f else 0f)
    }


    LaunchedEffect(item.isChecked) {
        if(item.isChecked){
            lineProgress.animateTo(
                1f,
                tween(500),
            )
        }else{
            lineProgress.animateTo(
                0f,
                tween(500)
            )
        }
    }

    Row(
        modifier
            .fillMaxWidth()
            .drawBehind {
                val trapeziumBox = trapeziumShape(8).createOutline(size, layoutDirection, density)
                drawOutline(trapeziumBox , background)
            }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier= Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            CheckboxCircular(
                checked = item.isChecked ,
                onCheck = {
                    onCheck(item.id , it)
                },
                onBackground = successGreen,
                background = MaterialTheme.colorScheme.surface
            )
            Text(
                item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = onBackground
                ),
                modifier = Modifier
                    .drawBehind {
                        drawStrikethroughLine(
                            strikeThroughColor,
                            progress = lineProgress.value,
                            alpha = 0.6f,
                            strokeWidth = 8f
                        )
                    }
                    .padding(horizontal = 5.dp)

            )
        }
        Box(
            Modifier.clip(CircleShape)
                .clickable {
                    onDelete(item.id)
                }
        ){
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "delete checklist item ${item.title} ?",
                tint = onBackground,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ChecklistItemPrev() {
    MaterialTheme {
        ChecklistItem(
            item = ChecklistEntity(title = "checklist", isChecked = true) ,
            onCheck = {_,_->} ,
            onDelete = {}
        )
    }
}