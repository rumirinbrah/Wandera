package com.zzz.feature_trip.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.TripWithDays
import com.zzz.feature_trip.home.presentation.components.TripItem
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    navToCreateTrip : ()->Unit,
    navToThemeSettings : ()->Unit,
    navToTripOverview : (tripId : Long)->Unit,
    onNavBarVisibilityChange : (visible : Boolean) ->Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {

    val tripsWithDoc by homeViewModel.tripWithDocs.collectAsStateWithLifecycle()

    HomePage(
        tripsWithDoc = tripsWithDoc,
        navToThemeSettings = navToThemeSettings,
        navToCreateTrip = navToCreateTrip,
        navToTripOverview = navToTripOverview,
        onNavBarVisibilityChange = {
            onNavBarVisibilityChange(it)
        }
    )
}

@Composable
private fun HomePage(
    tripsWithDoc : List<TripWithDays>,
    navToCreateTrip : ()->Unit,
    navToThemeSettings : ()->Unit,
    navToTripOverview : (tripId : Long)->Unit,
    onNavBarVisibilityChange : (visible : Boolean) ->Unit
) {
    val lazyListState = rememberLazyListState()

    val isAtTop = remember {
        derivedStateOf{
            lazyListState.firstVisibleItemIndex == 0
        }
    }
    LaunchedEffect(isAtTop.value) {
        if(isAtTop.value){
            delay(300)
            onNavBarVisibilityChange(isAtTop.value)
        }else{
            onNavBarVisibilityChange(isAtTop.value)
        }
    }


    Box(Modifier.fillMaxSize()){
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularIconButton(
                modifier = Modifier.align(Alignment.Start),
                icon = com.zzz.core.R.drawable.round_more_vert_24 ,
                contentDescription = "Go to theme settings" ,
                onClick = {
                    navToThemeSettings()
                },
                background = Color.Gray.copy(0.4f),
                onBackground = Color.White,
                buttonSize = 40.dp,
                iconSize = 20.dp
            )
            //HEADER
            Row(
                Modifier.fillMaxWidth() ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    VerticalSpace(10.dp)
                    Text(
                        "WELCOME," ,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Traveller",
                        fontSize = 17.sp,
                    )
                }
                CircularIconButton(
                    icon = com.zzz.core.R.drawable.add ,
                    contentDescription = "Create new trip" ,
                    onClick = {
                        navToCreateTrip()
                    },
                    iconSize = 30.dp
                )

            }
            VerticalSpace()
            Text(
                "Adventures" ,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            //TRIPS
            LazyColumn(
                Modifier.fillMaxWidth()
                    .heightIn(max = 800.dp) ,
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    tripsWithDoc,
                    key = {it.trip.id}
                ){trip->
                    //println(trip.trip.tripName)
                    TripItem(
                        tripWithDays = trip,
                        onClick = {id->
                            navToTripOverview(id)
                        },
                        modifier = Modifier.animateItem()
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

@Preview
@Composable
private fun HomePrev() {
    WanderaTheme {
        //HomePage(navToCreateTrip = {})
    }
}