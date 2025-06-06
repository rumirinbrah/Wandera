package com.zzz.core.presentation.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import com.zzz.core.presentation.nav.util.Screen
import com.zzz.feature_trip.create.presentation.CreateRoot
import com.zzz.feature_trip.create.presentation.CreateViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeNavGraph(navController : NavHostController){
    navigation<Screen.HomeGraph>(
        startDestination = Screen.HomeGraph
    ){

        //home
        composable<Screen.HomeGraph.HomeScreen> {

        }
        //home -> create
        composable<Screen.HomeGraph.CreateTripScreen> {backStack->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val vm = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)
            CreateRoot(vm)
        }
        //create -> add day
        composable<Screen.HomeGraph.AddDayScreen> {backStack->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(Screen.HomeGraph)
            }
            val vm = koinViewModel<CreateViewModel>(viewModelStoreOwner = parentEntry)
            val state by vm.tripState.collectAsStateWithLifecycle()

            Box(Modifier.fillMaxSize()){
                Text(state.tripTitle)
            }
        }
    }

}
class niga  :ViewModel(){

}