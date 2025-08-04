package com.zzz.core.presentation.text_field

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

/**
 * A custom wrapper around the OutlinedTextField.
 * @param value String value
 * @param placeholder Helper text
 * @param onValueChange Callback
 * @param singleLine Single line
 * @param maxLines Max num lines
 * @param imeAction Action for the keyboard
 * @param onDone Called for ime action DONE
 * @param hideKeyboardOnDone When true, will hide the keyboard on DONE action. Note that onDone callback will not be called.
 * @param keyboardType Type of keyboard
 * @param trailingIcon Icon at the end of text field
 * @author zyzz
 */
@Composable
fun RoundedTextField(
    value :String ,
    placeholder :String ,
    onValueChange : (String)->Unit ,
    singleLine : Boolean = false ,
    enabled : Boolean = true ,
    maxLines : Int = 10 ,
    imeAction : ImeAction = ImeAction.Unspecified ,
    onDone : ()->Unit = {},
    hideKeyboardOnDone : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Unspecified,
    @DrawableRes trailingIcon : Int? = null ,
    trailingIconDescription : String? = null ,
    background : Color = MaterialTheme.colorScheme.primaryContainer ,
    onBackground : Color = MaterialTheme.colorScheme.onPrimaryContainer ,
    shape: Shape = RoundedCornerShape(40) ,
    textStyle : TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = background,
            focusedContainerColor = background,
            focusedIndicatorColor = background,
            unfocusedIndicatorColor = background,
            disabledContainerColor = background,
            disabledTextColor = onBackground
        ),
        placeholder = {
            Text(
                placeholder ,
                color = onBackground.copy(0.5f),
                fontSize = textStyle.fontSize
            )
        },
        textStyle = textStyle,
        singleLine = singleLine,
        enabled = enabled,
        maxLines = maxLines,
        shape = shape,
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = trailingIconDescription
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                //keyboard?.hide()
                if(hideKeyboardOnDone){
                    keyboard?.hide()
                }else{
                    onDone()
                }
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Words,
            keyboardType = keyboardType
        )
    )
}
/**
 * A custom wrapper around the OutlinedTextField.
 * @param value String value
 * @param placeholder Helper annotated text
 * @param onValueChange Callback
 * @param singleLine Single line
 * @param maxLines Max num lines
 * @param imeAction Action for the keyboard
 * @param onDone Called for ime action DONE
 * @param hideKeyboardOnDone When true, will hide the keyboard on DONE action. Note that onDone callback will not be called.
 * @param keyboardType Type of keyboard
 * @param trailingIcon Icon at the end of text field
 * @author zyzz
 */
@Composable
fun RoundedTextField(
    value : String ,
    placeholder : AnnotatedString ,
    onValueChange : (String)->Unit ,
    singleLine : Boolean = false ,
    enabled : Boolean = true ,
    maxLines : Int = 10 ,
    imeAction : ImeAction = ImeAction.Unspecified ,
    onDone : ()->Unit = {},
    hideKeyboardOnDone : Boolean = false,
    keyboardType : KeyboardType = KeyboardType.Unspecified,
    @DrawableRes trailingIcon : Int? = null ,
    trailingIconDescription : String? = null ,
    background : Color = MaterialTheme.colorScheme.primaryContainer ,
    onBackground : Color = MaterialTheme.colorScheme.onPrimaryContainer ,
    placeholderColor : Color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
    shape: Shape = RoundedCornerShape(40) ,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = background,
            focusedContainerColor = background,
            focusedIndicatorColor = background,
            unfocusedIndicatorColor = background,
            disabledContainerColor = background,
            disabledTextColor = onBackground
        ),
        placeholder = {
            Text(placeholder, color = placeholderColor)
        },
        singleLine = singleLine,
        enabled = enabled,
        maxLines = maxLines,
        shape = shape,
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = trailingIconDescription
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                if(hideKeyboardOnDone){
                    keyboard?.hide()
                }else{
                    onDone()
                }
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Words,
            keyboardType = keyboardType
        )
    )
}