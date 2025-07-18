package com.zzz.core.presentation.tabrow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.zzz.core.presentation.components.DualOptionSelector

@Composable
fun WanderaTabRow(
    currentTab: Int = 0 ,
    totalTabs: Int = 2 ,
    modifier: Modifier = Modifier ,
    background: Color = MaterialTheme.colorScheme.surfaceContainer ,
    indicatorColor: Color = MaterialTheme.colorScheme.primary ,
    content: @Composable() (() -> Unit)
) {
    Box(
        modifier
            .background(background)
            .drawBehind {
                val offsetBy = size.width / totalTabs
                val start = offsetBy * currentTab
                drawLine(
                    color = indicatorColor ,
                    start = Offset(x = start , y = size.height) ,
                    end = Offset(x = start + offsetBy , y = size.height)
                )
            } ,
        contentAlignment = Alignment.Center
    ) {
        Row {
            content()
        }
    }
}