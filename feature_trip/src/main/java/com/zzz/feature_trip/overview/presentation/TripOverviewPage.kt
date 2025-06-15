package com.zzz.feature_trip.overview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.feature_trip.overview.presentation.components.ItineraryPager
import com.zzz.feature_trip.overview.presentation.components.PagerBottomIndicator
import com.zzz.feature_trip.overview.presentation.components.PagerItem
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun TripOverviewRoot(
    overviewViewModel: OverviewViewModel = koinViewModel(),
    navigateToDayDetails : ()->Unit
) {
    val state by overviewViewModel.overviewState.collectAsStateWithLifecycle()

    TripOverviewPage(
        state,
        onAction = {action->
            overviewViewModel.onAction(action)
        },
        navigateToDayDetails = navigateToDayDetails
    )
}

@Composable
private fun TripOverviewPage(
    state : OverviewState,
    onAction : (OverviewActions)->Unit,
    navigateToDayDetails : ()->Unit
) {
    val pagerState = rememberPagerState() {
        state.days.size
    }


    Box(
        Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        if(state.loading){
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                DotsLoadingAnimation()
            }
        }else{
            Column(
                Modifier.fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    state.trip?.tripName ?:"null",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                VerticalSpace(10.dp)
                Text("20 May - 25 May 2025")

                VerticalSpace()
                Text(
                    "Itinerary",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                ItineraryPager(
                    pagerState = pagerState,
                    days = state.days,
                    onDayClick = {
                        //println("In overview pg, the day is ${it.locationName}")
                        onAction(OverviewActions.UpdateSelectedDay(it))
                        navigateToDayDetails()
                    }
                )


            }
        }

    }


}