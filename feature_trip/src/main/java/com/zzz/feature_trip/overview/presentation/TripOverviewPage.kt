package com.zzz.feature_trip.overview.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.DateHeader
import com.zzz.feature_trip.overview.presentation.components.ItineraryLayoutOptions
import com.zzz.feature_trip.overview.presentation.components.ItineraryPager
import com.zzz.feature_trip.overview.presentation.components.OverviewDayItem
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

                VerticalSpace()
                DateHeader(
                    startDate = state.trip?.startDate ?: 0,
                    endDate = state.trip?.endDate ?: 0
                )

                VerticalSpace()
                ItineraryLayoutOptions(
                    isPagerLayout = state.itineraryPagerLayout,
                    onLayoutChange = {
                        onAction(OverviewActions.ChangeItineraryLayout)
                    }
                )
                AnimatedContent(
                    state.itineraryPagerLayout,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    }
                ) {pagerLayout->
                    if(pagerLayout){
                        ItineraryPager(
                            pagerState = pagerState,
                            days = state.days,
                            onDayClick = {
                                onAction(OverviewActions.UpdateSelectedDay(it))
                                navigateToDayDetails()
                            }
                        )
                    }else{
                        LazyColumn(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                state.days,
                                key = {it.id}
                            ){day->
                                OverviewDayItem(
                                    day ,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }


                VerticalSpace()
                //delete
                NormalButton(
                    modifier = Modifier.fillMaxWidth(),
                    background = MaterialTheme.colorScheme.errorContainer,
                    onBackground = MaterialTheme.colorScheme.onErrorContainer,
                    title = "Delete Trip",
                    onClick = {

                    }
                )

            }
        }

    }


}