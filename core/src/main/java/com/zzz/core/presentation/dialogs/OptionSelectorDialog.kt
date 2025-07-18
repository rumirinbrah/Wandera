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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zzz.core.domain.DailyTask
import com.zzz.core.presentation.components.DualOptionSelector
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme
import com.zzz.core.theme.successGreen

/**
 * @author zyzz
 */
@Composable
fun OptionSelectorDialog(
    title: String ,
    textFieldPlaceholder: String ,
    onDone: (title: String , isTodo: Boolean) -> Unit ,
    onDismiss : ()->Unit,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var text by remember { mutableStateOf("") }
    var isTodo by remember { mutableStateOf(true) }


    LaunchedEffect(keyboard) {
        keyboard?.let {
            println("MF visible")
        }
    }
    Dialog(
        onDismissRequest = {
            onDismiss()
            text = ""
        }
    ) {
        Column(
            modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerticalSpace(15.dp)
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)

            OutlinedTextField(
                value = text,
                onValueChange ={text=it},
                placeholder = { Text(textFieldPlaceholder) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusRequester.freeFocus()
                        focusManager.clearFocus()
                        keyboard?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words
                ),
                shape = RoundedCornerShape(35),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer ,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester),

            )

            DualOptionSelector(
                background = MaterialTheme.colorScheme.surfaceContainer,
                selectedColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    isTodo = it==DailyTask.TODO
                },
                modifier = Modifier
            )

            VerticalSpace(5.dp)

            Button(
                modifier = Modifier.widthIn(100.dp,150.dp),
                onClick = {
                    onDone(text.trim(),isTodo)
                    text = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = successGreen
                ),
                enabled = text.trim().isNotEmpty()
            ) {
                Text("Done", color = Color.White)
            }


        }
    }
}

@Preview
@Composable
private fun DialogPrev() {
    WanderaTheme {
        OptionSelectorDialog(
            title = "Enter title" ,
            textFieldPlaceholder = "Title" ,
            onDone = { s: String , b: Boolean -> } ,
            onDismiss = {}
        )
    }
}