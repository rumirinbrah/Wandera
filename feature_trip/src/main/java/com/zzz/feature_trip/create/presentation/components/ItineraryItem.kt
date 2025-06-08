package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.data.trip.DayWithTodos

@Composable
fun ItineraryItem(
    dayWithTodos: DayWithTodos ,
    onClick : (id : Long)->Unit,
    onEdit : (id : Long)->Unit,
    onDelete : (id : Long)->Unit,
    modifier: Modifier = Modifier
) {
    val day = remember { dayWithTodos.day }
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = 70.dp, max = 100.dp)
            .clip(Shapes().large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                onClick(day.id)
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            day.locationName ,
            fontSize = 17.sp ,
            fontWeight = FontWeight.Bold ,
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            IconButton(
                onClick = {
                    onEdit(day.id)
                }
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.edit_day) ,
                    contentDescription = "Edit ${day.locationName}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick ={
                    onDelete(day.id)
                }
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.delete) ,
                    contentDescription = "Delete ${day.locationName}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}