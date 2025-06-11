package com.zzz.wandera.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zzz.core.presentation.nav.util.Screen
import com.zzz.wandera.nav.util.BottomNavBar

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    var navBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        Modifier.fillMaxSize()
    ) { innerPadding->

        Box(
            Modifier.fillMaxSize()
                .padding(innerPadding)
        ){
            NavHost(
                navController = navController,
                startDestination = Screen.HomeGraph
            ) {

                //HOME
                homeNavGraph(
                    navController,
                    navBarVisible = {
                        navBarVisible = it
                    }
                )

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
            AnimatedVisibility(
                navBarVisible,
                enter = slideInVertically(
                    initialOffsetY = {
                        it/2
                    }
                ) ,
                exit =  slideOutVertically(
                    targetOffsetY = {
                        it
                    }
                ) ,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavBar(
                    navController,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }

        }
    }
}