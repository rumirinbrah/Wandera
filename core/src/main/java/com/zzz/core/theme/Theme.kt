package com.zzz.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = lightPrimary ,
    secondary = lightPrimary ,
    tertiary = lightPrimary
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
    darkTheme: Boolean = isSystemInDarkTheme() ,
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme ,
        typography = Typography ,
        content = content
    )
}