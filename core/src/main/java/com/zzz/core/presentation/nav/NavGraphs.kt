package com.zzz.core.presentation.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import com.zzz.core.presentation.nav.util.Screen

fun NavGraphBuilder.homeNavGraph(navController : NavHostController){
    navigation<Screen.HomeGraph>(
        startDestination = Screen.HomeGraph
    ){
        //home
        composable<Screen.HomeGraph.HomeScreen> {

        }
        //home -> create
        composable<Screen.HomeGraph.CreateTripScreen> {

        }
        //create -> add day
        composable<Screen.HomeGraph.AddDayScreen> {

        }
    }

}