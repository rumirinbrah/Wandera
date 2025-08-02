package com.zzz.core.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.modifiers.generalDialogProperties
import com.zzz.core.theme.successGreen

/**
 * @author zyzz
 * @param title Text for the dialog
 * @param textFieldPlaceholder Helper textr
 * @param onDone Returns the text field text
 * @param onDismiss Dismiss
 * @param dismissEnabled On/Off dismiss
 * @param singleLine
 */
@Composable
fun DialogWithTextField(
    title: String ,
    textFieldPlaceholder: String ,
    onDone: (title: String) -> Unit ,
    onDismiss: () -> Unit ,
    dismissEnabled: Boolean = true ,
    singleLine : Boolean = true,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current

    var text by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            if (dismissEnabled) {
                onDismiss()
                text = ""
            }
        }
    ) {
        Column(
            modifier
                .generalDialogProperties(MaterialTheme.colorScheme.surface) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerticalSpace(15.dp)
            Text(title , fontWeight = FontWeight.Bold , fontSize = 18.sp)

            VerticalSpace(5.dp)

//            RoundedTextField(
//                value = text,
//                onValueChange = {text = it},
//                placeholder = textFieldPlaceholder,
//                imeAction = ImeAction.Done,
//                shape = RoundedCornerShape(35),
//                singleLine = singleLine,
//                modifier = Modifier
//            )

            OutlinedTextField(
                value = text ,
                onValueChange = { text = it } ,
                placeholder = { Text(textFieldPlaceholder) } ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words
                ) ,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                    }
                ) ,
                shape = RoundedCornerShape(35) ,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer ,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer ,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer ,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer
                ) ,
                singleLine = singleLine,
                modifier = Modifier
            )



            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    modifier = Modifier.widthIn(100.dp , 150.dp) ,
                    onClick = {
                        onDismiss()
                    } ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer ,
                    )
                ) {
                    Text("Cancel" , color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    modifier = Modifier.widthIn(100.dp , 150.dp) ,
                    onClick = {
                        onDone(text.trim())
                        text = ""
                    } ,
                    enabled = text.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = successGreen ,
                    )
                ) {
                    Text(
                        "Done" ,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }
    }
}