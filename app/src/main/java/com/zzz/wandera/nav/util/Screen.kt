package com.zzz.wandera.nav.util

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
        data class UpdateTripScreen(val tripId : Long)

        /**
         * Will be used for both adding and updating a day
         */
        @Serializable
        data class AddDayScreen(
            val tripId: Long = 0,
            val isUpdating: Boolean = false,
            val dayId : Long = 0
        )

        @Serializable
        data object DayDetailsScreen

        @Serializable
        data class TripOverviewScreen(val tripId : Long)

    }

    @Serializable
    data object RecentsScreen : Screen()

    @Serializable
    data object TranslateScreen : Screen()

    @Serializable
    data object ThemeScreen : Screen()

}