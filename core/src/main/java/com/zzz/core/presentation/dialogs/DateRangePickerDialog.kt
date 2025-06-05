package com.zzz.core.presentation.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onSelect :(start : Long,end : Long)->Unit,
    onDismiss : ()->Unit,
) {
    val datePickerState = rememberDateRangePickerState(

    )
    val context = LocalContext.current

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if(datePickerState.selectedStartDateMillis != null && datePickerState.selectedEndDateMillis !=null){
                        onSelect(datePickerState.selectedStartDateMillis!!,datePickerState.selectedEndDateMillis!!)
                    }else{
                        Toast.makeText(context , "Please select valid dates!" , Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Done")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            datePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            showModeToggle = true,
            headline = {
                Text("Select Dates")
            },
            title = null
        )
    }

}