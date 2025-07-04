package com.zzz.feature_trip.recents.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.zzz.feature_trip.recents.presentation.components.EmptyIndicator

@Composable
fun RecentsRoot(modifier: Modifier = Modifier) {
    RecentsPage(
        modifier
    )
}

@Composable
private fun RecentsPage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize()
    ){
        EmptyIndicator(
            Modifier.align(Alignment.Center)
        )
    }
}