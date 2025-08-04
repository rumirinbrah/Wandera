package com.zzz.wandera.presentation.nav

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.zzz.core.presentation.modifiers.customShadow
import com.zzz.core.presentation.theme_change.ChangeThemePage
import com.zzz.core.presentation.toast.rememberWanderaToastState
import com.zzz.feature_settings.presentation.AppSettingsRoot
import com.zzz.feature_translate.presentation.TranslateRoot
import com.zzz.feature_translate.presentation.viewmodel.TranslationViewModel
import com.zzz.wandera.presentation.nav.util.BottomNavBar
import com.zzz.wandera.presentation.nav.util.Screen
import com.zzz.wandera.ui.ThemeState
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    themeState: ThemeState ,
    toggleDarkMode: (Boolean) -> Unit ,
    tripJsonUri : Uri? = null
) {
    val navController = rememberNavController()
    val wanderaToastState = rememberWanderaToastState()

    var navBarVisible by remember { mutableStateOf(true) }
    var navBarHeight by remember { mutableStateOf(0.dp) }
    var currentRoute by remember { mutableStateOf<Screen>(Screen.HomeGraph) }

    LaunchedEffect(Unit) {
        if(tripJsonUri!=null){
            navController.navigate(Screen.HomeGraph.ImportTripScreen)
        }
    }
    Scaffold(
        Modifier.fillMaxSize() ,
    ) { innerPadding ->

        Box(
            Modifier.fillMaxSize()
            //.padding(innerPadding)
        ) {
            NavHost(
                navController = navController ,
                startDestination = Screen.HomeGraph
            ) {

                composable<Screen.SettingsScreen> {
                    LaunchedEffect(Unit) {
                        navBarVisible = false
                    }

                    AppSettingsRoot(
                        modifier = Modifier.padding(innerPadding),
                        navToThemeSettings = {
                            navController.navigate(Screen.ThemeScreen)
                        },
                        navUp = {
                            navController.navigateUp()
                        }
                    )
                }
                //HOME
                homeNavGraph(
                    navController ,
                    wanderaToastState = wanderaToastState,
                    tripJsonUri = tripJsonUri,
                    navBarVisible = {
                        navBarVisible = it
                    } ,
                    innerPadding = innerPadding
                )

                //recents
                recentsNavGraph(
                    navController = navController,
                    navBarVisible = {
                        navBarVisible = it
                    },
                    innerPadding = innerPadding
                )

                //TRANSLATE
                composable<Screen.TranslateScreen> { backStack ->

                    val parentEntry = remember(backStack) {
                        navController.getBackStackEntry(Screen.TranslateScreen)
                    }
                    val translateVm =
                        koinViewModel<TranslationViewModel>(viewModelStoreOwner = parentEntry)

                    TranslateRoot(
                        Modifier.padding(innerPadding) ,
                        navBarVisible = {
                            navBarVisible = it
                        } ,
                        navigateUp = {
                            navController.navigateUp()
                        } ,
                        bottomPadding = navBarHeight ,
                        translationViewModel = translateVm
                    )
                }
                //THEME
                composable<Screen.ThemeScreen> {
                    LaunchedEffect(Unit) {
                        navBarVisible = false
                    }
                    ChangeThemePage(
                        night = themeState.isDarkMode ,
                        toggleDarkMode = toggleDarkMode ,
                        innerPadding = innerPadding
                    )
                }
            }
            AnimatedVisibility(
                navBarVisible ,
                enter = slideInVertically(
                    initialOffsetY = {
                        it / 2
                    } ,
                    animationSpec = tween()
                ) ,
                exit = slideOutVertically(
                    targetOffsetY = {
                        it * 2
                    }
                ) ,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                BottomNavBar(
                    navController ,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(vertical = 8.dp , horizontal = 4.dp)
                        .customShadow(
                            color = MaterialTheme.colorScheme.surfaceContainer ,
                            offsetY = 10f ,
                        ) ,
                    currentRoute = currentRoute ,
                    onHeightCalculated = { height ->
                        navBarHeight = height
                    } ,
                    onRouteChange = {
                        currentRoute = it
                    }
                )
            }


        }
    }
}