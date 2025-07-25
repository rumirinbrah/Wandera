package com.zzz.feature_trip.overview.presentation.tabs.note_expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.image_picker.components.CustomTab

/**
 * Tab row to switch between Expense Note & Expense Tracker!
 */
@Composable
fun NoteExpenseTabRow(
    currentTab: Int ,
    onTabChange: (Int) -> Unit ,
    modifier: Modifier = Modifier ,
    containerColor : Color = MaterialTheme.colorScheme.background ,
    onContainerColor : Color = MaterialTheme.colorScheme.onBackground ,
) {
    Row (
        modifier
            .background(containerColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        CustomTab(
            title = "Notes" ,
            tabNo = 0 ,
            onClick = onTabChange,
            selected = currentTab == 0,
            verticalPadding = 0.dp,
            drawIndicator = true
        )

        CustomTab(
            title = "Expenses" ,
            tabNo = 1 ,
            onClick = onTabChange,
            selected = currentTab == 1,
            verticalPadding = 0.dp,
            drawIndicator = true
        )
    }
}