package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.zzz.data.trip.model.Day
import kotlin.math.absoluteValue

/**
 * @author zyzz
 */
@Composable
internal fun ItineraryPager(
    pagerState: PagerState,
    days : List<Day>,
    onDayClick : (Day)->Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalPager(
            modifier = modifier
                .heightIn(400.dp),
            state = pagerState,
            key = {
                days[it].id
            },
            pageSpacing = 10.dp
        ) {page->
            val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue

            PagerItem(
                day = days[page] ,
                onClick = onDayClick,
                modifier = Modifier
                    .graphicsLayer {
                        val scale =(1-pageOffset).coerceIn(0.95f,1f)
                        scaleX = scale
                        scaleY = scale

                    }
            )
        }

        PagerBottomIndicator(
            pagerState.currentPage,
            pageCount = days.size,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}