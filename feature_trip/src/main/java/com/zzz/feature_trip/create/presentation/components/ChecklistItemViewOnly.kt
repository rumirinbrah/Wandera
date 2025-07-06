package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zzz.data.note.model.ChecklistEntity

/**
 * Used to display checklist item in create page. Simply displays the title and a delete icon
 */
@Composable
internal fun ChecklistItemViewOnly(
    item : ChecklistEntity,
    onDelete : (itemId : Long)->Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier
            .border(1.dp , MaterialTheme.colorScheme.onBackground , MaterialTheme.shapes.small)
            .padding(vertical = 8.dp , horizontal = 16.dp) ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            item.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =  Modifier.weight(1f)
        )
        Box(
            Modifier.clip(CircleShape)
                .clickable { onDelete(item.id) }
        ){
            Icon(
                imageVector =Icons.Default.Close ,
                contentDescription = "Delete ${item.title} ?"

            )
        }
    }
}