package com.zzz.wandera.nav

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.zzz.core.presentation.nav.util.Screen
import com.zzz.feature_trip.create.presentation.AddDayRoot
import com.zzz.feature_trip.create.presentation.CreateRoot
import com.zzz.feature_trip.create.presentation.CreateViewModel
import com.zzz.feature_trip.create.presentation.DayDetailsRoot
import com.zzz.feature_trip.home.presentation.HomeRoot
import com.zzz.feature_trip.home.presentation.HomeViewModel
import com.zzz.feature_trip.overview.presentation.OverviewActions
import com.zzz.feature_trip.overview.presentation.OverviewViewModel
import com.zzz.feature_trip.overview.presentation.TripOverviewRoot
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeNavGraph(
    navController : NavHostController,
    navBarVisible : (Boolean)->Unit
){
    navigation<Screen.HomeGraph>(
        startDestination = Screen.HomeGraph.HomeScreen
    ){
        //home
        composable<Screen.HomeGraph.HomeScreen> {backStack->

            LaunchedEffect(Unit) {
                navBarVisible(true)
            }
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val homeViewModel = koinViewModel<HomeViewModel>(viewModelStoreOwner = parentEntry)

            HomeRoot(
                navToCreateTrip = {
                    navController.navigate(Screen.HomeGraph.CreateTripScreen)
                },
                navToThemeSettings = {
                    navController.navigate(Screen.ThemeScreen)
                },
                onNavBarVisibilityChange = {visible->
                    println("calling visibility change for $visible")
                    navBarVisible(visible)
                },
                navToTripOverview = {id->
                    navController.navigate(Screen.HomeGraph.TripOverviewScreen(id))
                },
                homeViewModel = homeViewModel
            )
        }
        //home -> create
        composable<Screen.HomeGraph.CreateTripScreen> {backStack->
            LaunchedEffect(Unit) {
                navBarVisible(false)
            }
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph.CreateTripScreen)
            }
            val createViewModel = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)


            CreateRoot(
                onNavToAddDay = {
                    navController.navigate(Screen.HomeGraph.AddDayScreen)
                },
                onEditDay = {
                    navController.navigate(Screen.HomeGraph.AddDayScreen)
                },
                navigateUp = {
                    navController.navigateUp()
                },
                createViewModel
            )
        }
        //create -> add day
        composable<Screen.HomeGraph.AddDayScreen> {backStack->
            /*
            not necessary to use remember here(doing for recovering process death scenarios)
             */

            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph.CreateTripScreen)
            }
            val createViewModel = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)


            AddDayRoot(
                navigateUp = {
                    navController.navigateUp()
                },
                createViewModel = createViewModel
            )

        }
        composable<Screen.HomeGraph.DayDetailsScreen> {backStack->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val createViewModel = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)
            DayDetailsRoot(
                navigateUp = {
                    navController.navigateUp()
                } ,
                createViewModel
            )
        }
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
                overviewViewModel
            )
        }
    }

}