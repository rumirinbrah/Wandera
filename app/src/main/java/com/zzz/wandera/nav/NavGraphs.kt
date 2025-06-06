package com.zzz.wandera.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zzz.core.presentation.nav.util.Screen
import com.zzz.feature_trip.create.presentation.AddDayRoot
import com.zzz.feature_trip.create.presentation.CreateRoot
import com.zzz.feature_trip.create.presentation.CreateViewModel
import com.zzz.feature_trip.create.presentation.states.CreateAction
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeNavGraph(navController : NavHostController){
    navigation<Screen.HomeGraph>(
        startDestination = Screen.HomeGraph.CreateTripScreen
    ){

        //home
        composable<Screen.HomeGraph.HomeScreen> {

        }
        //home -> create
        composable<Screen.HomeGraph.CreateTripScreen> {backStack->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val createViewModel = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)


            CreateRoot(
                onNavToAddDay = {
                    navController.navigate(Screen.HomeGraph.AddDayScreen)
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
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val createViewModel = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)


            AddDayRoot(
                onDiscard = {
                    //clear day state
                    navController.navigateUp()
                    createViewModel.onAction(CreateAction.OnDiscard)
                } ,
                saveAndNavigateUp = {
                    //already saved, nav up
                    navController.navigateUp()
                },
                createViewModel = createViewModel
            )

        }
    }

}