package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    //pagerState: PagerState,
    days: List<Day> ,
    loading: Boolean = false ,
    onDayClick: (Day) -> Unit ,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState() {
        days.size
    }
    val pagerFling = PagerDefaults.flingBehavior(
        state = pagerState ,
        pagerSnapDistance = PagerSnapDistance.atMost(
            pages = if (days.size > 12) {
                3
            } else {
                1
            }
        )
    )


    Column(
        Modifier.animateContentSize() ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            days.isEmpty() && !loading -> {
                Text(
                    "Seems like you haven't added anything..." ,
                    modifier = Modifier.align(Alignment.CenterHorizontally) ,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                )
            }

            else -> {
                HorizontalPager(
                    modifier = modifier
                        .heightIn(400.dp) ,
                    state = pagerState ,
                    key = {
                        days[it].id
                    } ,
                    pageSpacing = 10.dp ,
                    flingBehavior = pagerFling
                ) { page ->
                    val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue

                    PagerItem(
                        day = days[page] ,
                        onClick = onDayClick ,
                        modifier = Modifier
                            .graphicsLayer {
                                val scale = (1 - pageOffset).coerceIn(0.95f , 1f)
                                scaleX = scale
                                scaleY = scale

                            }
                    )
                }

                PagerBottomIndicator(
                    pagerState.currentPage ,
                    pageCount = days.size ,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

    }

}