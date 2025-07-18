package com.zzz.feature_trip.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.feature_trip.create.util.toFormattedDate

@Composable
fun DateComponent(
    startDate : Long,
    endDate : Long,
    modifier: Modifier = Modifier,
    fontWeight : FontWeight? = null,
    fontSize : TextUnit = TextUnit.Unspecified,
    background : Color = MaterialTheme.colorScheme.background,
    onBackground : Color = MaterialTheme.colorScheme.onBackground,
) {
    Box(
        modifier
            .clip(Shapes().large)
            .background(background)
            .padding(8.dp)
    ){
        Text(
            text = startDate.toFormattedDate() +"- ${endDate.toFormattedDate("dd MMM yy")}",
            color = onBackground,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}