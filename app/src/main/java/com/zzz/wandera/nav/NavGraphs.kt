package com.zzz.wandera.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.zzz.core.presentation.toast.WanderaToastState
import com.zzz.wandera.nav.util.Screen
import com.zzz.feature_trip.create.presentation.AddDayRoot
import com.zzz.feature_trip.create.presentation.CreateRoot
import com.zzz.feature_trip.create.presentation.viewmodel.CreateViewModel
import com.zzz.feature_trip.create.presentation.viewmodel.CreateAction
import com.zzz.feature_trip.create.presentation.viewmodel.DayViewModel
import com.zzz.feature_trip.overview.presentation.DayDetailsRoot
import com.zzz.feature_trip.home.presentation.HomeRoot
import com.zzz.feature_trip.home.presentation.HomeViewModel
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewActions
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import com.zzz.feature_trip.overview.presentation.TripOverviewRoot
import com.zzz.feature_trip.update.presentation.UpdateRoot
import com.zzz.feature_trip.update.presentation.viewmodel.UpdateTripViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeNavGraph(
    navController : NavHostController,
    wanderaToastState: WanderaToastState,
    navBarVisible : (Boolean)->Unit,
    innerPadding : PaddingValues = PaddingValues(0.dp)
){
    navigation<Screen.HomeGraph>(
        startDestination = Screen.HomeGraph.HomeScreen
    ){
        //home
        composable<Screen.HomeGraph.HomeScreen> { backStack->

            LaunchedEffect(Unit) {
                navBarVisible(true)
            }
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val homeViewModel = koinViewModel<HomeViewModel>(viewModelStoreOwner = parentEntry)

            HomeRoot(
                modifier = Modifier.padding(innerPadding),
                navToCreateTrip = {
                    navController.navigate(Screen.HomeGraph.CreateTripScreen())
                },
                navToThemeSettings = {
                    navController.navigate(Screen.ThemeScreen)
                },
                onNavBarVisibilityChange = {visible->
                    navBarVisible(visible)
                },
                navToTripOverview = {id->
                    navController.navigate(Screen.HomeGraph.TripOverviewScreen(id))
                },
                homeViewModel = homeViewModel
            )
        }
        //home -> create
        composable<Screen.HomeGraph.CreateTripScreen> { backStack->

            val route = backStack.toRoute<Screen.HomeGraph.CreateTripScreen>()

            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph.CreateTripScreen(route.tripId))
            }
            val createViewModel = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)

            LaunchedEffect(Unit) {
                navBarVisible(false)
                if(route.tripId==null){
                    createViewModel.onAction(CreateAction.TripActions.CreateSession)
                }else{
                    println("TRIP ID is ${route.tripId}")
                    createViewModel.onAction(CreateAction.TripActions.FetchTripData(route.tripId))
                }
            }
            CreateRoot(
                Modifier.padding(innerPadding),
                onNavToAddDay = {tripId->
                    navController.navigate(Screen.HomeGraph.AddDayScreen(tripId = tripId))
                },
                onEditDay = {dayId->
                    navController.navigate(Screen.HomeGraph.AddDayScreen(isUpdating = true , dayId = dayId))
                },
                navigateUp = {
                    navController.navigateUp()
                },
                createViewModel
            )
        }
        //Update
        composable<Screen.HomeGraph.UpdateTripScreen> { backStack->
            val route = backStack.toRoute<Screen.HomeGraph.UpdateTripScreen>()
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph.UpdateTripScreen(route.tripId))
            }
            val updateTripViewModel = koinViewModel<UpdateTripViewModel>(viewModelStoreOwner = parentEntry)

            LaunchedEffect(Unit) {
                updateTripViewModel.onAction(
                    CreateAction
                        .TripActions
                        .FetchTripData(route.tripId)
                )
            }

            UpdateRoot(
                modifier = Modifier.padding(innerPadding),
                onNavToAddDay = {
                    navController.navigate(Screen.HomeGraph.AddDayScreen)
                } ,
                onEditDay = {
                    navController.navigate(Screen.HomeGraph.AddDayScreen)
                } ,
                navigateUp = {
                    navController.navigateUp()
                },
                updateTripViewModel = updateTripViewModel
            )
        }
        //create -> add day
        composable<Screen.HomeGraph.AddDayScreen> { backStack->

            val route = backStack.toRoute<Screen.HomeGraph.AddDayScreen>()

            val dayViewModel = koinViewModel<DayViewModel>()

            //ADD LAUNCHED EFFECT HERE
            LaunchedEffect(Unit) {
                if(route.isUpdating){
                    dayViewModel.onAction(
                        CreateAction
                            .DayActions
                            .FetchDayById(route.dayId)
                    )
                }else{
                    dayViewModel.onAction(
                        CreateAction
                            .DayActions
                            .CreateDaySession(route.tripId)
                    )
                }
            }
            AddDayRoot(
                Modifier.padding(innerPadding),
                navigateUp = {
                    navController.navigateUp()
                },
                dayViewModel = dayViewModel
            )

        }
        //-------- DETAILS --------
        composable<Screen.HomeGraph.DayDetailsScreen> { backStack->
            LaunchedEffect(Unit) {
                navBarVisible(false)
            }

            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val overviewViewModel = koinViewModel<OverviewViewModel>(viewModelStoreOwner = parentEntry)
            DayDetailsRoot(
                //Modifier.padding(innerPadding),
                navigateUp = {
                    navController.navigateUp()
                } ,
                overviewViewModel = overviewViewModel
            )
        }

        //-------- overview --------
        composable<Screen.HomeGraph.TripOverviewScreen> { backStack->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val overviewViewModel = koinViewModel<OverviewViewModel>(viewModelStoreOwner = parentEntry)
            val route = backStack.toRoute<Screen.HomeGraph.TripOverviewScreen>()


            LaunchedEffect(Unit) {
                navBarVisible(false)
                overviewViewModel.onAction(OverviewActions.FetchTripData(route.tripId))
            }

            TripOverviewRoot(
                Modifier.padding(innerPadding),
                overviewViewModel = overviewViewModel,
                wanderaToastState = wanderaToastState,
                navigateToDayDetails = {
                    navController.navigate(Screen.HomeGraph.DayDetailsScreen)
                },
                navToEditTrip = {tripId->
                    navController.navigate(Screen.HomeGraph.CreateTripScreen(tripId)){
                        navController.popBackStack()
                    }
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }

}