package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zzz.data.trip.model.Day

@Composable
internal fun ItineraryList(
    days : List<Day>,
    onClick :(Day)->Unit,
    markDayStatus: (isDone: Boolean , dayId: Long) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier.fillMaxWidth()
            .heightIn(max = 800.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            days,
            key = {it.id}
        ){day->
            OverviewDayItem(
                day ,
                onClick = {
                    onClick(it)
                },
                markDayStatus = markDayStatus,
                modifier = Modifier.animateItem()
            )
        }
    }
}