package com.zzz.wandera.presentation.nav.util

import androidx.annotation.DrawableRes
import com.zzz.core.R

data class NavItem(
    @DrawableRes val icon : Int,
    val title : String,
    val route : Screen

)
internal val bottomItems = listOf(
    NavItem(
        R.drawable.bike_nav,
        "Trips",
        Screen.HomeGraph
    ) ,
    NavItem(
        R.drawable.alarm_nav,
        "Recents",
        Screen.RecentsScreen
    ) ,
    NavItem(
        R.drawable.translate_nav,
        "Translate",
        Screen.TranslateScreen
    )
)