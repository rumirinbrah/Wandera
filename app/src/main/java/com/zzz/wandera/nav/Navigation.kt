package com.zzz.wandera.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zzz.wandera.nav.util.Screen
import com.zzz.core.presentation.theme_change.ChangeThemePage
import com.zzz.wandera.nav.util.BottomNavBar
import com.zzz.wandera.ui.ThemeState

@Composable
fun Navigation(
    themeState : ThemeState,
    toggleDarkMode : (Boolean)->Unit,
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
                        println("visibility change $it")
                        navBarVisible = it
                    }
                )

                //RECENTS
                composable<Screen.RecentsScreen> {
                    Box(Modifier.fillMaxSize()){
                        Column {
                            Button(
                                onClick = {
                                }
                            ) {
                                Text("Not default")
                            }
                        }

                    }
                }
                //TRANSLATE
                composable<Screen.TranslateScreen> {

                }
                //THEME
                composable<Screen.ThemeScreen> {
                    LaunchedEffect(Unit) {
                        navBarVisible = false
                    }
                    ChangeThemePage(
                        night = themeState.isDarkMode,
                        toggleDarkMode = toggleDarkMode
                    )
                }
            }
            AnimatedVisibility(
                navBarVisible,
                enter = slideInVertically(
                    initialOffsetY = {
                        it/2
                    },
                    animationSpec = tween()
                ) ,
                exit =  slideOutVertically(
                    targetOffsetY = {
                        it*2
                    }
                ) ,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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