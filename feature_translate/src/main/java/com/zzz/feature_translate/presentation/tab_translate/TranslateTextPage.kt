package com.zzz.feature_translate.presentation.tab_translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.presentation.tab_translate.components.DropDownList
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState

@Composable
fun TranslateTextPage(
    state: TranslateState ,
    models : List<TranslationModel>,
    onAction: (TranslateAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    val tempList = remember {
        listOf(
            TranslationModel(
                languageCode = "en",
                name = "English"
            ),
            TranslationModel(
                languageCode = "jp",
                name = "Japan"
            ),
            TranslationModel(
                languageCode = "hn",
                name = "Hindi"
            ),
            TranslationModel(
                languageCode = "ru",
                name = "Ruski"
            )
        )
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Powered by Google",
            modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        Row(
            Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropDownList(
                titleInitial = "From",
                title = state.srcLanguage,
                items = tempList,
                onClick = {name, modelCode ->
                    onAction(TranslateAction.TranslatorAction.ChangeSrcLanguage(name, modelCode))
                },
                modifier = Modifier.weight(1f)
            )
            DropDownList(
                titleInitial = "To",
                title = state.destLanguage,
                items = tempList,
                onClick = {name, modelCode ->
                    onAction(TranslateAction.TranslatorAction.ChangeDestLanguage(name, modelCode))
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
