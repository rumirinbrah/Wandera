package com.zzz.wandera.presentation.nav.util

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object HomeGraph : Screen(){

        @Serializable
        data object HomeScreen

        @Serializable
        data class CreateTripScreen(val tripId: Long? = null)

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
        data class DayDetailsScreen(val dayId: Long)

        @Serializable
        data class TripOverviewScreen(val tripId : Long)

        @Serializable
        data class ExportTripScreen(val tripId: Long)

        @Serializable
        data object ImportTripScreen

    }

    @Serializable
    data object RecentsGraph : Screen(){
        @Serializable
        data object RecentsScreen : Screen()
        @Serializable
        data class RecentOverviewScreen(val tripId :Long) : Screen()
        @Serializable
        data class DayDetailsScreen(val dayId : Long)
    }


    @Serializable
    data object TranslateScreen : Screen()

    @Serializable
    data object ThemeScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()

}