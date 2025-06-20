package com.zzz.core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme

/**
 * @author zyzz
 */
@Composable
fun ConfirmActionDialog(
    title : String ,
    actionText : String,
    onConfirm:()->Unit ,
    onCancel:()->Unit ,
    modifier: Modifier = Modifier
)
{


    Dialog(
        onDismissRequest = {
            onCancel()
        }
    ) {
        Column(
            modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerticalSpace(5.dp)
            Text(
                title ,
                fontWeight = FontWeight.Bold ,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text("Cancel",color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(actionText, color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeletePrev() {
    WanderaTheme {
        ConfirmActionDialog(
            title = "Are yo sure u wann delete?",
            actionText = "Discard",
            onConfirm = {},
            onCancel = {}
        )
    }
}