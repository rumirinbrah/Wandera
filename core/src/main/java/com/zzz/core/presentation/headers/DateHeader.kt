package com.zzz.core.presentation.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.time.LocalDate
import java.util.Date
import java.util.Locale

/**
 * Date header with calendar icon
 */
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
        modifier
            .clip(MaterialTheme.shapes.large)
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

/**
 * Date header representing date as Month and Date in a column
 * @param dateFontSize Size for the date
 * @param monthFontSize Size for the month name
 */
@Composable
fun VerticalDateHeader(
    date : LocalDate ,
    dateFontSize: TextUnit = 25.sp ,
    monthFontSize: TextUnit = 14.sp ,
    fontWeight: FontWeight = FontWeight.Bold ,
    textColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.8f) ,
    containerColor : Color = MaterialTheme.colorScheme.primaryContainer ,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .background(
                containerColor ,
                RoundedCornerShape(topEnd = 20.dp , bottomEnd = 20.dp)
            )
            .padding(vertical = 4.dp , horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.month.name.take(3),
            color = textColor,
            fontSize = monthFontSize,
            modifier = modifier
        )
        Text(
            "${date.dayOfMonth}",
            fontSize = dateFontSize,
            fontWeight = fontWeight,
            color = textColor,
            modifier = modifier
        )
    }

}

/**
 * Simple date with a background
 */
@Composable
fun DateText(
    modifier: Modifier = Modifier,
    timestamp : Long = System.currentTimeMillis(),
    fontSize : TextUnit = 13.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color : Color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
) {
    val text = remember {
        timestamp.toFormattedDate("dd MMM yyyy")
    }
    Box(modifier.padding(8.dp)){
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color,
            modifier = modifier
        )
    }
}

/**
 * Format long value to a date
 */
private fun Long.toFormattedDate(format : String = "dd MMM") : String{
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date(this))
}