package com.zzz.feature_trip.home.presentation

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.TripWithDays
import com.zzz.feature_trip.home.presentation.components.HomeTopBar
import com.zzz.feature_trip.home.presentation.components.TripItemRoot
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    modifier: Modifier = Modifier,
    navToCreateTrip : ()->Unit,
    navToThemeSettings : ()->Unit,
    navToTripOverview : (tripId : Long)->Unit,
    onNavBarVisibilityChange : (visible : Boolean) ->Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {

    val tripsWithDoc by homeViewModel.tripWithDocs.collectAsStateWithLifecycle()
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    HomePage(
        modifier,
        tripsWithDoc = tripsWithDoc,
        state = state,
        navToThemeSettings = navToThemeSettings,
        navToCreateTrip = navToCreateTrip,
        navToTripOverview = navToTripOverview,
        onNavBarVisibilityChange = {
            onNavBarVisibilityChange(it)
        }
    )
    //Tester(modifier)
}

@Composable
private fun HomePage(
    modifier: Modifier = Modifier,
    tripsWithDoc : List<TripWithDays>,
    state : HomeState,
    navToCreateTrip : ()->Unit,
    navToThemeSettings : ()->Unit,
    navToTripOverview : (tripId : Long)->Unit,
    onNavBarVisibilityChange : (visible : Boolean) ->Unit
) {
    val lazyListState = rememberLazyListState()

    val isAtTop = remember {

//        derivedStateOf{
//            println("Is at top remember")
//            lazyListState.firstVisibleItemIndex == 0
//        }
        mutableStateOf(true)
    }
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex==0
        }.collect{
            isAtTop.value = it
        }
    }
    LaunchedEffect(isAtTop.value) {
        if(isAtTop.value){
            //if at top, delay a lil and show navbar
            delay(300)
        }
        onNavBarVisibilityChange(isAtTop.value)
    }


    Box(modifier.fillMaxSize()){
        when{
            /*
            !state.loading && tripsWithDoc.isEmpty()->{
                Text(
                    "Woah there's nothing here...tap on the + icon to get started.",
                    modifier = Modifier.align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                )
            }

             */
            state.loading ->{
                Box(
                    Modifier.align(Alignment.Center)
                ){
                    DotsLoadingAnimation()
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
//                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            /*
            CircularIconButton(
                modifier = Modifier.align(Alignment.Start),
                icon = com.zzz.core.R.drawable.settings ,
                contentDescription = "Go to theme settings" ,
                onClick = {
                    navToThemeSettings()
                },
                background = MaterialTheme.colorScheme.surfaceContainer,
                onBackground = MaterialTheme.colorScheme.onBackground,
                buttonSize = 40.dp,
                iconSize = 25.dp
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
                //create new
                CircularIconButton(
                    modifier = Modifier
                        .customShadow(
                            color = MaterialTheme.colorScheme.onBackground,
                            shadowRadius = 15f,
                            offsetY = 5f,
                            alpha = 0.2f
                        ),
                    icon = com.zzz.core.R.drawable.add ,
                    contentDescription = "Create new trip" ,
                    onClick = {
                        navToCreateTrip()
                    },
                    iconSize = 30.dp
                )

            }

             */
            AnimatedVisibility(isAtTop.value) {
                HomeTopBar(
                    navToThemeSettings = navToThemeSettings,
                    navToCreateTrip = navToCreateTrip
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
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 800.dp) ,
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    tripsWithDoc,
                    key = {it.trip.id}
                ){trip->
                    TripItemRoot(
                        trip,
                        onClick = {id->
                            navToTripOverview(id)
                        },
                        modifier = Modifier.animateItem(),
                        ticketLikeContainer = state.homeItemTicketLikeContainer
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

@Composable
fun Tester(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    val list = remember {  mutableStateListOf<String>() }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoundedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder= "Enter",
            imeAction = ImeAction.Done,
            onDone = {
                list.add(text)
                text = ""
            }
        )
        LazyColumn {
            items(list){
                Text(it , fontSize = 25.sp , fontWeight = FontWeight.Bold)
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