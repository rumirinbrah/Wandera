package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.data.trip.DayWithTodos

@Composable
fun ItineraryItem(
    dayWithTodos: DayWithTodos ,
    onClick : (id : Long)->Unit,
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "DAY ${day.dayNo} - ${day.locationName}" ,
            fontSize = 17.sp ,
            fontWeight = FontWeight.Bold ,
        )
    }
}