package com.zzz.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = darkBackground,
    onBackground = darkOnBackground,
    surface = darkSurface,
    onSurface = darkOnSurface,
    surfaceContainer = darkSurfaceContainer,
    onSurfaceVariant = darkOnSurfaceContainer,
    primary = darkPrimary ,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnPrimaryContainer,
    secondary = darkSecondary,
    onSecondary = darkOnSecondary,
    tertiary = lightPrimary,
    errorContainer = darkErrorContainer,
    onErrorContainer = darkOnErrorContainer
)

private val LightColorScheme = lightColorScheme(
    background = lightBackground,
    onBackground = lightOnBackground,
    surface = lightSurface,
    onSurface = lightOnSurface,
    surfaceContainer = lightSurfaceContainer,
    onSurfaceVariant = lightOnSurfaceContainer,
    primary = lightPrimary ,
    onPrimary = lightOnPrimary,
    primaryContainer = lightPrimaryContainer,
    onPrimaryContainer = lightOnPrimaryContainer,
    secondary = lightSecondary ,
    onSecondary = lightOnSecondary,
    tertiary = lightPrimary,
    errorContainer = lightErrorContainer,
    onErrorContainer = lightOnErrorContainer

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun WanderaTheme(
    useSystemTheme : Boolean = true,
    darkThemePref : Boolean = false,
    content: @Composable () -> Unit
) {
    val isSystemInDark : Boolean = isSystemInDarkTheme()

    val darkTheme = if(useSystemTheme){
        isSystemInDark
    }else{
        darkThemePref
    }
    val view = LocalView.current
    val window = (view.context as Activity?)?.window

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    LaunchedEffect(darkTheme) {
        //wait...how tf does this make sense
        window?.let {
            val controller = WindowCompat.getInsetsController(it,view)//.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme

        }
    }

    MaterialTheme(
        colorScheme = colorScheme ,
        typography = Typography ,
        content = content
    )
}