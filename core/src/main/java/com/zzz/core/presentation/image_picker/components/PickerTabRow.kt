package com.zzz.core.presentation.image_picker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A tab row for wandera image picker. Has two tabs- Recents, Albums
 */
@Composable
internal fun PickerTabRow(
    currentTab: Int ,
    onTabChange: (Int) -> Unit ,
    modifier: Modifier = Modifier,
    containerColor : Color = MaterialTheme.colorScheme.surfaceContainer,
    onContainerColor : Color = MaterialTheme.colorScheme.onBackground,

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
        CustomTab(
            title = "Recent",
            onClick = onTabChange,
            selected = currentTab == 0,
            tabNo = 0,
            modifier = Modifier.fillMaxWidth()
        )

        CustomTab(
            title = "Albums",
            onClick = onTabChange,
            selected = currentTab == 1,
            tabNo = 1,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

/**
 * Represents a tab in the tab row
 */
@Composable
fun CustomTab(
    title :String,
    selected : Boolean = false,
    tabNo : Int,
    onClick : (Int)->Unit,
    selectedTabColor :Color = MaterialTheme.colorScheme.onBackground,
    unselectedTabColor :Color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
    verticalPadding : Dp = 16.dp,
    drawIndicator : Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .padding(vertical = verticalPadding)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    onClick(tabNo)
                }
            )
            .drawBehind {
                if(selected && drawIndicator){
                    drawLine(
                        selectedTabColor,
                        start = Offset(0f,size.height) ,
                        end = Offset(size.width,size.height),
                        strokeWidth = 5f
                    )
                }
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            title,
            color = if (selected) {
                selectedTabColor
            } else {
                unselectedTabColor
            },
            fontWeight = FontWeight.Medium
        )
    }
}