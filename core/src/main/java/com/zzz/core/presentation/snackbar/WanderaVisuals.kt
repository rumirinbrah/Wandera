package com.zzz.core.presentation.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.Color

internal class WanderaVisuals(
    override val actionLabel: String? ,
    override val duration: SnackbarDuration ,
    override val message: String ,
    override val withDismissAction: Boolean,
    val background : Color? = null,
    val onBackground : Color? = null,
) : SnackbarVisuals

suspend fun SnackbarHostState.showWanderaSnackbar(
    message : String,
    actionLabel : String = "Dismiss",
    duration : SnackbarDuration = SnackbarDuration.Short,
    withDismissAction: Boolean = false,
    background: Color? = null,
    onBackground: Color? = null,
){
    showSnackbar(
        WanderaVisuals(
            actionLabel,
            duration,
            message,
            withDismissAction,
            background,
            onBackground
        )
    )
}