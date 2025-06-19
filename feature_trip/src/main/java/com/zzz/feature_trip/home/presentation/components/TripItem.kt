package com.zzz.feature_trip.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.TripWithDays
import com.zzz.feature_trip.home.util.getDateDifference

@Composable
fun TripItem(
    tripWithDays: TripWithDays ,
    onClick: (tripId : Long) -> Unit ,
    modifier: Modifier = Modifier
) {
    val horizontalGradient = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primaryContainer ,
            MaterialTheme.colorScheme.background ,
        )
    )
    val dayImages = remember(tripWithDays.days) {
        tripWithDays.days.map {
            it.image
        }
    }
    val trip = remember { tripWithDays.trip }
    var duration by remember { mutableStateOf("Duration...") }

    LaunchedEffect(Unit) {
        val diff = getDateDifference(trip.startDate,trip.endDate)
        duration = "Duration $diff days"
    }
    Box(
        modifier
            .clip(RoundedCornerShape(15))
            .fillMaxWidth()
            .background(horizontalGradient)
            .clickable(
                onClick = {
                    onClick(trip.id)
                }
            )
            .padding(16.dp)

    ) {
        Row(
            Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    trip.tripName ,
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    duration ,
                    fontSize = 14.sp ,
                )
                VerticalSpace(5.dp)
                if(dayImages.any { it!=null }){
                    OverlappedImagesRow(
                        images = dayImages,
                        imageSize = 50.dp
                    )
                }
                VerticalSpace(10.dp)
                DateComponent(
                    startDate = trip.startDate,
                    endDate = trip.endDate
                )

            }

            Icon(
                painter = painterResource(com.zzz.core.R.drawable.arrow_right_round) ,
                contentDescription = null ,
                modifier = Modifier.size(35.dp)
            )

        }


    }
}

@Preview
@Composable
private fun TripItemPRev() {

    WanderaTheme {
        //TripItem(onClick = {})
    }
}