package com.zzz.core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zzz.core.presentation.components.VerticalSpace

@Composable
fun DialogWithTextField(
    title: String ,
    textFieldPlaceholder: String ,
    onDone: (title: String) -> Unit ,
    onDismiss : ()->Unit,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current

    var text by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            onDismiss()
            text = ""
        }
    ) {
        Column(
            modifier
                .clip(Shapes().large)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerticalSpace(15.dp)
            Text(title , fontWeight = FontWeight.Bold , fontSize = 18.sp)

            VerticalSpace(5.dp)

            OutlinedTextField(
                value = text ,
                onValueChange = { text = it } ,
                placeholder = { Text(textFieldPlaceholder) } ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
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
                modifier = Modifier
            )

            Button(
                modifier = Modifier.widthIn(100.dp,150.dp),
                onClick = {
                    onDone(text.trim())
                    text = ""
                },
                enabled = text.trim().isNotEmpty()
            ) {
                Text("Done")
            }
        }
    }
}