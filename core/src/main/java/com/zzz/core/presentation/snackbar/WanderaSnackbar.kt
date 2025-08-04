package com.zzz.core.presentation.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Custom wandera snackbar
 */
@Composable
fun WanderaSnackbar(
    snackbarData: SnackbarData,
    background : Color = MaterialTheme.colorScheme.primaryContainer,
    onBackground : Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    val visuals = remember { snackbarData.visuals as WanderaVisuals }
    Row (
        Modifier.fillMaxWidth()
            .clip(Shapes().large)
            .background(visuals.background ?: background)
            .padding(horizontal = 12.dp, vertical = 8.dp) ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            visuals.message,
            fontWeight = FontWeight.Bold,
            color = visuals.onBackground ?: onBackground,
            modifier = Modifier.weight(1f)
        )
        visuals.actionLabel?.let { action->
            TextButton(
                onClick = {
                    snackbarData.dismiss()
                }
            ) {
                Text(
                    action ,
                    color = visuals.onBackground?.copy(0.7f) ?: onBackground.copy(0.7f)
                )
            }
        }
    }
}