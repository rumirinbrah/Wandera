package com.zzz.core.presentation.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.theme.successGreen

/**
 * Quite unstable and has a messy layout as of now.
 * @author zyzz
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onSelect: (start: Long , end: Long) -> Unit ,
    onDismiss: () -> Unit ,
) {
    val datePickerState = rememberDateRangePickerState(

    )
    val context = LocalContext.current
    val containerColor = MaterialTheme.colorScheme.surfaceContainer


    DatePickerDialog(
        onDismissRequest = onDismiss ,
        confirmButton = {
            NormalButton(
                title = "Done" ,
                onClick = {
                    if (datePickerState.selectedStartDateMillis != null && datePickerState.selectedEndDateMillis != null) {
                        onSelect(
                            datePickerState.selectedStartDateMillis!! ,
                            datePickerState.selectedEndDateMillis!!
                        )
                    } else {
                        Toast.makeText(context , "Please select valid dates!" , Toast.LENGTH_SHORT)
                            .show()
                    }
                } ,
                background = successGreen ,
                onBackground = Color.White
            )
        } ,
        dismissButton = {
            NormalButton(
                title = "Cancel" ,
                onClick = {
                    onDismiss()

                } ,
                background = MaterialTheme.colorScheme.primaryContainer ,
                onBackground = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } ,
        colors = DatePickerDefaults.colors(
            containerColor = containerColor
        )
    ) {
        DateRangePicker(
            datePickerState ,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp) ,
            showModeToggle = true ,
            headline = {
                Text(
                    "Select Start and End date" ,
                    modifier = Modifier.padding(8.dp)
                )
            } ,
            title = null ,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }


}