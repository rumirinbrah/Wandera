package com.zzz.feature_settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.feature_settings.R
import com.zzz.feature_settings.presentation.components.SettingsItem
import com.zzz.feature_settings.presentation.components.SettingsSection

@Composable
fun AppSettingsRoot(
    modifier: Modifier = Modifier
) {
    AppSettingsPage(
        modifier = modifier
    )
}

@Composable
fun AppSettingsPage(
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Box(
        modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            Modifier
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButtonHeader(
                actionIcon = com.zzz.core.R.drawable.arrow_back ,
                title = "App Settings" ,
                fontSize = 20.sp,
                itemsSpacing = 8.dp,
                fontWeight = FontWeight.Bold,
                onAction = {
                    //nav up
                }
            )
            VerticalSpace()

            SettingsSection(
                sectionTitle = "Customize Layout"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.list_layout,
                    title = "Home layout settings",
                    subTitle = "Change the way items appear on your home screen!",
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    shadowOffsetY = 10f,
                    shadowAlpha = 0.3f,
                    onClick = {

                    }
                )
            }

            SettingsSection(
                sectionTitle = "Customize Theme"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.moon_icon,
                    iconTint = Color.Blue,
                    title = "Theme",
                    subTitle = "Dark theme, Light theme, its up to you!",
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    shadowOffsetY = 10f,
                    shadowAlpha = 0.3f,
                    onClick = {

                    }
                )
            }

            SettingsSection(
                sectionTitle = "Feedback"
            ) {
                SettingsItem(
                    icon = com.zzz.core.R.drawable.info,
                    iconTint = Color(0xFFB04E4E) ,
                    title = "Have any suggestions?",
                    subTitle = "Would love to hear from you, be it feature requests or bugs.",
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    shadowOffsetY = 10f,
                    shadowAlpha = 0.3f,
                    onClick = {

                    }
                )
            }

        }
    }
}

@Preview
@Composable
private fun AppSeettingsPrev() {
    MaterialTheme {
        AppSettingsRoot()
    }
}
