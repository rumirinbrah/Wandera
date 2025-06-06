package com.zzz.core.presentation.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.zzz.core.presentation.nav.util.Screen

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {

        }
    ) { innerPadding->

        Box(
            Modifier.padding(innerPadding)
        ){
            NavHost(
                navController = navController,
                startDestination = Screen.HomeGraph
            ) {

                //HOME
                homeNavGraph(navController)

                //RECENTS
                composable<Screen.RecentsScreen> {
                    Box(Modifier.fillMaxSize()){
                        Button(
                            onClick = {
                                navController.navigate(Screen.HomeGraph)
                            }
                        ) {
                            Text("NAVIGATE")
                        }
                    }
                }
                //TRANSLATE
                composable<Screen.TranslateScreen> {

                }
            }
        }
    }
}