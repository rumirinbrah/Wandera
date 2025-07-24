package com.zzz.feature_settings.presentation.util

import kotlinx.serialization.Serializable

@Serializable
internal sealed class SettingScreen {

    @Serializable
    data object SettingsRoot : SettingScreen()

    @Serializable
    data object HomeLayoutSettings : SettingScreen()

    @Serializable
    data object ChecklistBoxSettings : SettingScreen()

    @Serializable
    data object ImportInstructionsPage : SettingScreen()

}