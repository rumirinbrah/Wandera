package com.zzz.core.presentation.text_field

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.sin

@Composable
fun RoundedTextField(
    value :String,
    placeholder :String,
    onValueChange : (String)->Unit,
    singleLine : Boolean = false,
    maxLines : Int = 10,
    background : Color = MaterialTheme.colorScheme.primaryContainer,
    onBackground : Color = MaterialTheme.colorScheme.onPrimaryContainer,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = background,
            focusedContainerColor = background,
            focusedIndicatorColor = background,
            unfocusedIndicatorColor = background
        ),
        placeholder = {
            Text(placeholder, color = onBackground.copy(0.5f))
        },
        singleLine = singleLine,
        maxLines = maxLines,
        shape = RoundedCornerShape(40)
    )
}