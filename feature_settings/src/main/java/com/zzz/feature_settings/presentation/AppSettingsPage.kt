package com.zzz.feature_settings.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.feature_settings.R
import com.zzz.feature_settings.presentation.components.SettingsItem
import com.zzz.feature_settings.presentation.components.SettingsSection
import com.zzz.feature_settings.presentation.layout.ChecklistBoxSettingsPage
import com.zzz.feature_settings.presentation.layout.HomeLayoutSettingsPage
import com.zzz.feature_settings.presentation.share.ImportInstructionsPage
import com.zzz.feature_settings.presentation.util.SettingScreen
import com.zzz.feature_settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppSettingsRoot(
    modifier: Modifier = Modifier ,
    navToThemeSettings :() ->Unit,
    navUp: () -> Unit
) {

    val navController = rememberNavController()

    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        modifier = modifier
            .padding(16.dp) ,
        navController = navController ,
        startDestination = SettingScreen.SettingsRoot
    ) {
        composable<SettingScreen.SettingsRoot> {
            AppSettingsPage(
                modifier = Modifier ,
                navUp = navUp,
                navToThemeSettings = navToThemeSettings,
                onNav = {route->
                    navController.navigate(route)
                }
            )
        }
        composable<SettingScreen.HomeLayoutSettings> {
            HomeLayoutSettingsPage(
                modifier = Modifier,
                state = state,
                onAction = {action->
                    settingsViewModel.onAction(action)
                },
                navUp = {
                    navController.navigateUp()
                }
            )
        }
        composable<SettingScreen.ChecklistBoxSettings> {
            ChecklistBoxSettingsPage(
                modifier = Modifier,
                state = state,
                onAction = {action->
                    settingsViewModel.onAction(action)
                },
                navUp = {
                    navController.navigateUp()
                }
            )
        }
        composable<SettingScreen.ImportInstructionsPage> {
            ImportInstructionsPage(
                modifier = Modifier,
                navUp = {
                    navController.navigateUp()
                }
            )
        }
    }


}

@Composable
internal fun AppSettingsPage(
    navUp: () -> Unit,
    navToThemeSettings: () -> Unit ,
    onNav : (SettingScreen)->Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()


    Box(
        modifier
            .fillMaxSize()
            .background(
            MaterialTheme.colorScheme.background
            )
    ) {
        Column(
            Modifier
                .verticalScroll(scrollState) ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButtonHeader(
                actionIcon = com.zzz.core.R.drawable.arrow_back ,
                title = "Wandera Settings" ,
                fontSize = 20.sp ,
                itemsSpacing = 8.dp ,
                fontWeight = FontWeight.Bold ,
                onAction = {
                    navUp()
                }
            )
            VerticalSpace()

            SettingsSection(
                sectionTitle = "Share Trips"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.share ,
                    title = "Importing trips in Wandera" ,
                    subTitle = "How in the world do I even add the trips shared with me?",
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    onClick = {
                        onNav(SettingScreen.ImportInstructionsPage)
                    },
                    iconTint = Color(0xFFFA7070)
                )
            }
            //layout
            SettingsSection(
                sectionTitle = "Customize Layout"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingsItem(
                        icon = com.zzz.core.R.drawable.list_layout ,
                        title = "Home layout settings" ,
                        subTitle = "Change the way items appear on your home screen!" ,
                        contentColor = MaterialTheme.colorScheme.onBackground ,
                        //shadowOffsetY = 5f ,
                        //shadowAlpha = 0.2f ,
                        onClick = {
//                            navToHomeLayoutSettings()
                            onNav(SettingScreen.HomeLayoutSettings)
                        }
                    )
                    SettingsItem(
                        icon = com.zzz.core.R.drawable.download_done ,
                        iconTint = Color(0xFF58E85E) ,
                        title = "Checklist container settings" ,
                        subTitle = "Change shape of the checklist containers." ,
                        contentColor = MaterialTheme.colorScheme.onBackground ,
                        onClick = {
//                            navToChecklistSettings()
                            onNav(SettingScreen.ChecklistBoxSettings)
                        }
                    )
                }

            }

            //--- THEME ---
            SettingsSection(
                sectionTitle = "Customize Theme"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.moon_icon ,
                    iconTint = Color(0xFFB23095) ,
                    title = "Theme" ,
                    subTitle = "Dark theme, Light theme, its up to you!" ,
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    onClick = {
                        navToThemeSettings()
                    }
                )
            }

            SettingsSection(
                sectionTitle = "Other"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingsItem(
                        icon = R.drawable.round_feedback_24 ,
                        iconTint = Color(0xFFB04E4E) ,
                        title = "Have any suggestions?" ,
                        subTitle = "Would love to hear from you, be it feature requests or bugs." ,
                        contentColor = MaterialTheme.colorScheme.onBackground ,
                        onClick = {

                        }
                    )

                    SettingsItem(
                        icon = R.drawable.star ,
                        iconTint = Color(0xFFF3E04D) ,
                        title = "Rate us on PlayStore" ,
                        subTitle = "It'll only take a few minutes, y'know?" ,
                        contentColor = MaterialTheme.colorScheme.onBackground ,
                        onClick = {

                        }
                    )
                }


            }



//            SettingsSection(
//                sectionTitle = "Enjoying the app?"
//            ) {
//
//            }

        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppSeettingsPrev() {
    MaterialTheme {
        AppSettingsRoot(
            navUp = {},
            navToThemeSettings = {}
        )
    }
}
