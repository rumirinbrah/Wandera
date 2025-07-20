package com.zzz.feature_translate.presentation.tab_translate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.ElevatedIconTextButton
import com.zzz.core.presentation.components.CheckboxCircular
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.successGreen
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.R
import com.zzz.feature_translate.presentation.tab_translate.components.DropDownList
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState

@Composable
fun TranslateTextPage(
    state: TranslateState ,
    models: List<TranslationModel> ,
    onAction: (TranslateAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    val availableModels = remember(models) {
        models.filter { it.downloaded }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Powered by Google" ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        //drop downs
        Row(
            Modifier.align(Alignment.CenterHorizontally) ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropDownList(
                titleInitial = "From" ,
                title = state.srcLanguage ,
                items = availableModels ,
                onClick = { name , modelCode ->
                    onAction(TranslateAction.TranslatorAction.ChangeSrcLanguage(name , modelCode))
                } ,
                modifier = Modifier.weight(1f)
            )
            DropDownList(
                titleInitial = "To" ,
                title = state.destLanguage ,
                items = availableModels ,
                onClick = { name , modelCode ->
                    onAction(TranslateAction.TranslatorAction.ChangeDestLanguage(name , modelCode))
                } ,
                modifier = Modifier.weight(1f)
            )
        }
        RoundedTextField(
            value = state.sourceText ,
            onValueChange = {
                onAction(TranslateAction.OnTextChange(it))
            } ,
            shape = RectangleShape ,
            placeholder = "Type something..." ,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp , max = 300.dp),
            textStyle = TextStyle(
                fontSize = 20.sp
            )
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            CheckboxCircular(
                checked = state.keepFormatting ,
                onCheck = {
                    onAction(TranslateAction.TranslatorAction.SetTextFormatting(it))
                } ,
                modifier = Modifier ,
                onBackground = successGreen ,
                background = MaterialTheme.colorScheme.surfaceContainer,
                iconSize = 20.dp
            )
            Text(
                "Keep text formatting" ,
                fontSize = 13.sp
            )
        }
        //---- Action Buttons ----
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp) ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.close ,
                text = "Clear" ,
                enabled = !state.translating,
                onClick = {
                    onAction(TranslateAction.OnClearText)
                }
            )
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.translate_nav ,
                text = "Translate" ,
                enabled = !state.translating,
                onClick = {
                    onAction(TranslateAction.TranslatorAction.TranslateText)
                }
            )
        }
        AnimatedVisibility(
            state.translating,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                "Translating please wait...",
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )
        }
        VerticalSpace(10.dp)
        SelectionContainer {
            Box(
                Modifier.clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(vertical = 8.dp , horizontal = 16.dp)
            ) {
                Text(
                    text = state.translatedText.ifEmpty {
                        "Translated text will appear here..."
                    } ,
                    fontSize = 20.sp
                )
            }

        }

    }
}
