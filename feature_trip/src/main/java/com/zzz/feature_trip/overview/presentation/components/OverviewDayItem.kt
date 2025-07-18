package com.zzz.feature_trip.overview.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.modifiers.drawStrikethroughLine
import com.zzz.data.trip.model.Day

/**
 * Used in column layout of itinerary in TripOverviewPage
 * @author zyzz
 */
@Composable
fun OverviewDayItem(
    day: Day ,
    onClick: (Day) -> Unit ,
    markDayStatus: (isDone: Boolean , dayId: Long) -> Unit ,
    modifier: Modifier = Modifier
) {

    val backgroundBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                Color.Black ,
                Color(0xBA09413A)
            )
        )
    }


    Row(
        modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(MaterialTheme.shapes.large)
            .paint(
                painter = painterResource(com.zzz.core.R.drawable.nature_lake) ,
                alpha = 0.3f ,
                contentScale = ContentScale.Crop
            )
            .background(backgroundBrush)
            .clickable {
                onClick(day)
            }
            .padding(16.dp) ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            day.locationName ,
            fontSize = 20.sp ,
            color = Color.White ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .alpha(if (day.isDone) 0.5f else 1f)
                .drawBehind {
                    if(day.isDone){
                        drawStrikethroughLine(
                            color = Color.White ,
                            progress = 1f ,
                            strokeWidth = 5f
                        )
                    }

                }
                .padding(horizontal = 4.dp)


        )
        CircularIconButton(
            icon = if (day.isDone) {
                com.zzz.core.R.drawable.arrow_undo
            } else {
                com.zzz.core.R.drawable.todo_tick
            } ,
            contentDescription = "Mark ${day.locationName} as done" ,
            onClick = {
                Log.d("done" , "OverviewDayItem: Marking ${day.locationName} as done")

                markDayStatus(!day.isDone , day.id)
            } ,
            background = Color.Gray.copy(0.5f) ,
            onBackground = Color.White ,
            buttonSize = 40.dp
        )
    }
}