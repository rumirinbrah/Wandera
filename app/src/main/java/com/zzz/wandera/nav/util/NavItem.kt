package com.zzz.wandera.nav.util

import androidx.annotation.DrawableRes
import com.zzz.core.R
import com.zzz.core.presentation.nav.util.Screen

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