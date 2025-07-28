package com.zzz.feature_trip.recents.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.feature_trip.recents.presentation.components.EmptyIndicator
import com.zzz.feature_trip.recents.presentation.components.RecentTripItem
import com.zzz.feature_trip.recents.presentation.viewmodel.RecentTripsState
import com.zzz.feature_trip.recents.presentation.viewmodel.RecentsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecentsRoot(
    recentsViewModel: RecentsViewModel = koinViewModel() ,
    navToOverview : (tripId : Long)->Unit,
    modifier: Modifier = Modifier
) {
    val state by recentsViewModel.state.collectAsStateWithLifecycle()

    RecentsPage(
        state = state,
        navToOverview = navToOverview,
        modifier = modifier
    )
}

@Composable
private fun RecentsPage(
    state : RecentTripsState,
    navToOverview : (tripId : Long)->Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    Box(
        modifier.fillMaxSize()
    ){
        when{
            state.loading ->{
                Box(
                    Modifier.align(Alignment.Center)
                ){
                    DotsLoadingAnimation()
                }
            }
            state.recents.isEmpty()->{
                EmptyIndicator(
                    Modifier.align(Alignment.Center)
                )
            }
            else->{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    VerticalSpace()
                    Text(
                        "Recent Adventures" ,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    VerticalSpace(5.dp)
                    //TRIPS
                    LazyColumn(
                        Modifier.fillMaxWidth()
                            .heightIn(max = 800.dp) ,
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            state.recents,
                            key = {it.trip.id}
                        ){trip->
                            RecentTripItem(
                                trip,
                                onClick = {id->
                                    navToOverview(id)
                                },
                                modifier = Modifier.animateItem(),
                            )

                        }

                        //space at bottom to avoid bottom bar overlap
                        item {
                            VerticalSpace(60.dp)
                        }

                    }
                }
            }
        }

    }
}