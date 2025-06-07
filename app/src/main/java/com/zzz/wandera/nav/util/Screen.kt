package com.zzz.core.presentation.nav.util

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object HomeGraph : Screen(){

        @Serializable
        data object HomeScreen

        @Serializable
        data object CreateTripScreen

        @Serializable
        data object AddDayScreen

        @Serializable
        data object DayDetailsScreen

    }

    @Serializable
    data object RecentsScreen : Screen()

    @Serializable
    data object TranslateScreen : Screen()

}