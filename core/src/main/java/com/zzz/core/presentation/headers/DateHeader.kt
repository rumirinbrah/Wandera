package com.zzz.core.presentation.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateHeader(
    startDate : Long,
    endDate : Long,
    fontSize : TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    background : Color = MaterialTheme.colorScheme.secondary.copy(0.4f),
    onBackground : Color = MaterialTheme.colorScheme.onSecondary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.clip(MaterialTheme.shapes.large)
            .background(background)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Icon(
            painter = painterResource(R.drawable.calendar) ,
            contentDescription = "Dates",
            tint = onBackground
        )
        Text(
            text = "${startDate.toFormattedDate()} - ${endDate.toFormattedDate("dd MMM yyyy")}",
            color = onBackground,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}
private fun Long.toFormattedDate(format : String = "dd MMM") : String{
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date(this))
}