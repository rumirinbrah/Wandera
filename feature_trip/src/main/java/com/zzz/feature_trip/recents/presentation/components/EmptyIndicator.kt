package com.zzz.feature_trip.recents.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.feature_trip.R

@Composable
internal fun EmptyIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.touching_grass),
            contentDescription = "Recent trips will appear here",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            "All your recent adventures will be here",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
        )
    }
}