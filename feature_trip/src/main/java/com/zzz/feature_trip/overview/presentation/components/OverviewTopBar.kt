package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.CircularIconButton

@Composable
internal fun OverviewTopBar(
    tripName : String = "" ,
    onBack : ()->Unit ,
    editTrip : ()->Unit ,
    modifier: Modifier = Modifier
) {

    Row(
        modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.large) ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CircularIconButton(
            modifier = Modifier
                .padding(4.dp) ,
            icon = com.zzz.core.R.drawable.arrow_back ,
            contentDescription = "Go back" ,
            background = Color.DarkGray.copy(0.4f) ,
            onBackground = Color.White,
            onClick = {
                onBack()
            }
        )
        Text(
            tripName,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        CircularIconButton(
            modifier = Modifier
                .padding(4.dp) ,
            icon = com.zzz.core.R.drawable.edit_day ,
            contentDescription = "Go back" ,
            background = Color.DarkGray.copy(0.4f) ,
            onBackground = Color.White,
            onClick = {
                editTrip()
            }
        )
    }
}