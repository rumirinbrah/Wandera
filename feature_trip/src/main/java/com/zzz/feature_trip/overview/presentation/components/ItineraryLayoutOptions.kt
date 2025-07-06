package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author zyzz
 * To switch between pager and list layout for the itinerary
 */
@Composable
internal fun ItineraryLayoutOptions(
    isPagerLayout : Boolean = true ,
    onLayoutChange: () -> Unit ,
    modifier: Modifier = Modifier
) {
    val contentColor = MaterialTheme.colorScheme.onBackground

    Row(
        modifier.fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            "Itinerary",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Box(
            modifier.clip(CircleShape)
                .clickable {
                    onLayoutChange()
                }
                .padding(8.dp)
        ){
            Icon(
                painter = painterResource(
                    if(isPagerLayout){
                        com.zzz.core.R.drawable.list_layout
                    }else{
                        com.zzz.core.R.drawable.circle_filled
                    }
                ) ,
                contentDescription = if(isPagerLayout){
                    "Change layout to pager"
                }else{
                    "Change layout to list"
                } ,
                tint = contentColor ,
                modifier = Modifier.size(20.dp)
            )
        }
    }

}
