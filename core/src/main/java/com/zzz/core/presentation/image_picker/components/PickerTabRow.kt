package com.zzz.core.presentation.image_picker.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun PickerTabRow(
    currentTab: Int ,
    onTabChange: (Int) -> Unit ,
    modifier: Modifier = Modifier,
    containerColor : Color = MaterialTheme.colorScheme.surfaceContainer,
    onContainerColor : Color = MaterialTheme.colorScheme.onBackground,
    selectedTabColor :Color = MaterialTheme.colorScheme.onBackground,
    unselectedTabColor :Color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
) {


    TabRow(
        modifier = modifier ,
        selectedTabIndex = currentTab ,
        containerColor = containerColor ,
        contentColor = onContainerColor ,
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
                    "Recent" ,
                    color = if (currentTab == 0) {
                        selectedTabColor
                    } else {
                        unselectedTabColor
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
                    "Albums",
                    color = if (currentTab == 1) {
                        selectedTabColor
                    } else {
                        unselectedTabColor
                    }
                )
            }
        )

    }
}