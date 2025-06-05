package com.zzz.core.presentation.nav.util

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object RecentsScreen : Screen()

    @Serializable
    data object TranslateScreen : Screen()

}