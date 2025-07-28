package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zzz.data.trip.model.Day

@Composable
internal fun ItineraryList(
    days : List<Day> ,
    onClick :(Day)->Unit,
    markDayStatus: (isDone: Boolean , dayId: Long) -> Unit,
    viewOnly : Boolean = false,
    modifier: Modifier = Modifier
) {

    val finishedDays = remember (days){
        days.filter { it.isDone }
    }
    val remainingDays = remember (days){
        days.filter { !it.isDone }
    }


    LazyColumn(
        modifier.fillMaxWidth()
            .heightIn(max = 800.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            if(days.isEmpty()){
                Text(
                    "Seems like you haven't added anything..." ,
                    modifier = Modifier ,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                )
            }
        }
        items(
            finishedDays,
            key = {
                it.id
            }
        ){day->
            OverviewDayItem(
                day ,
                onClick = {
                    onClick(it)
                },
                markDayStatus = markDayStatus,
                modifier = Modifier.animateItem(),
                viewOnly = viewOnly
            )
        }
        items(
            remainingDays,
            key = {it.id}
        ){day->
            OverviewDayItem(
                day ,
                onClick = {
                    onClick(it)
                },
                markDayStatus = markDayStatus,
                modifier = Modifier.animateItem(),
                viewOnly = viewOnly
            )
        }
    }
}