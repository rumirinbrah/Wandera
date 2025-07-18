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
import com.zzz.core.theme.successGreen
import com.zzz.feature_settings.R
import com.zzz.feature_settings.presentation.components.SettingsItem
import com.zzz.feature_settings.presentation.components.SettingsSection
import com.zzz.feature_settings.presentation.layout.ChecklistBoxSettingsPage
import com.zzz.feature_settings.presentation.layout.HomeLayoutSettingsPage
import com.zzz.feature_settings.presentation.util.SettingScreen
import com.zzz.feature_settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppSettingsRoot(
    modifier: Modifier = Modifier ,
    navUp: () -> Unit
) {

    val navController = rememberNavController()

    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController ,
        startDestination = SettingScreen.SettingsRoot
    ) {
        composable<SettingScreen.SettingsRoot> {
            AppSettingsPage(
                modifier = modifier ,
                navUp = {},
                navToHomeLayoutSettings = {
                    navController.navigate(SettingScreen.HomeLayoutSettings)
                } ,
                navToChecklistSettings = {
                    navController.navigate(SettingScreen.ChecklistBoxSettings)
                } ,
            )
        }
        composable<SettingScreen.HomeLayoutSettings> {
            HomeLayoutSettingsPage(
                modifier = modifier
            )
        }
        composable<SettingScreen.ChecklistBoxSettings> {
            ChecklistBoxSettingsPage(
                modifier = modifier
            )
        }
    }


}

@Composable
internal fun AppSettingsPage(
    navUp: () -> Unit,
    navToHomeLayoutSettings: () -> Unit ,
    navToChecklistSettings: () -> Unit ,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .padding(16.dp)
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

            //layout
            SettingsSection(
                sectionTitle = "Customize Layout"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.list_layout ,
                    title = "Home layout settings" ,
                    subTitle = "Change the way items appear on your home screen!" ,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer ,
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    shadowOffsetY = 10f ,
                    shadowAlpha = 0.3f ,
                    onClick = {
                        navToHomeLayoutSettings()
                    }
                )
                VerticalSpace(8.dp)
                SettingsItem(
                    icon = com.zzz.core.R.drawable.download_done ,
                    iconTint = successGreen ,
                    title = "Checklist container settings" ,
                    subTitle = "Change shape of the checklist containers." ,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer ,
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    shadowOffsetY = 10f ,
                    shadowAlpha = 0.3f ,
                    onClick = {
                        navToChecklistSettings()
                    }
                )
            }

            SettingsSection(
                sectionTitle = "Customize Theme"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.moon_icon ,
                    iconTint = Color(0xFFA94BB9) ,
                    title = "Theme" ,
                    subTitle = "Dark theme, Light theme, its up to you!" ,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer ,
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    shadowOffsetY = 10f ,
                    shadowAlpha = 0.3f ,
                    onClick = {

                    }
                )
            }

            SettingsSection(
                sectionTitle = "Feedback"
            ) {
                SettingsItem(
                    icon = R.drawable.round_feedback_24 ,
                    iconTint = Color(0xFFB04E4E) ,
                    title = "Have any suggestions?" ,
                    subTitle = "Would love to hear from you, be it feature requests or bugs." ,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer ,
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    shadowOffsetY = 10f ,
                    shadowAlpha = 0.3f ,
                    onClick = {

                    }
                )
            }

            SettingsSection(
                sectionTitle = "Enjoying the app?"
            ) {
                SettingsItem(
                    icon = R.drawable.star ,
                    iconTint = Color(0xFFD9C843) ,
                    title = "Rate us on PlayStore" ,
                    subTitle = "It'll only take a few minutes, y'know?" ,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer ,
                    contentColor = MaterialTheme.colorScheme.onBackground ,
                    shadowOffsetY = 10f ,
                    shadowAlpha = 0.3f ,
                    onClick = {

                    }
                )
            }

        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppSeettingsPrev() {
    MaterialTheme {
        AppSettingsRoot(
            navUp = {}
        )
    }
}
