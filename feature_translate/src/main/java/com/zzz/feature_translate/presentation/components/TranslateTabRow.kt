package com.zzz.feature_translate.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@Composable
internal fun TranslateTabRow(
    currentTab: Int ,
    onTabChange: (Int) -> Unit ,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.onBackground
    val unselectedColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)

    TabRow(
        modifier = modifier ,
        selectedTabIndex = currentTab ,
        containerColor = MaterialTheme.colorScheme.background ,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer ,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                color = MaterialTheme.colorScheme.primary ,
                modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab])
            )
        }
    ) {
        Tab(
            selected = currentTab == 0 ,
            onClick = {
                onTabChange(0)
            } ,
            text = {
                Text(
                    "Download models" ,
                    color = if (currentTab == 0) {
                        selectedColor
                    } else {
                        unselectedColor
                    }
                )
            }
        )
        Tab(
            selected = currentTab == 1 ,
            onClick = {
                onTabChange(1)
            } ,
            text = {
                Text(
                    "Translate",
                    color = if (currentTab == 1) {
                        selectedColor
                    } else {
                        unselectedColor
                    }
                )
            }
        )

    }
}