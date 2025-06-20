package com.zzz.feature_translate.presentation.tab_download.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.data.translate.model.TranslationModel

/**
 * Represents a translation model in the lazy list
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TranslateModelItem(
    model: TranslationModel ,
    onLongClick :(modelCode : String, name : String)->Unit ,
    onDownloadModel :(modelCode : String) ->Unit ,
    modifier: Modifier = Modifier
) {
    val fontWeight = remember(model.downloaded) {
        if(model.downloaded){
            FontWeight.Bold
        }else{
            null
        }
    }
    val icon = remember(model.downloaded) {
        if(model.downloaded){
            com.zzz.core.R.drawable.download_done
        }else{
            com.zzz.core.R.drawable.download
        }
    }

    Row (
        modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .combinedClickable(
                enabled = model.downloaded,
                onClick = {},
                onLongClick = {
                    onLongClick(model.languageCode , model.name)
                },
                onLongClickLabel = "Delete ${model.name}"
            )
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            model.name,
            fontSize = 16.sp,
            fontWeight = fontWeight,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(
            onClick = {
                onDownloadModel(model.languageCode)
            },
            enabled = !model.downloaded
        ) {
            Icon(
                painter = painterResource(icon) ,
                contentDescription = if(model.downloaded) "Delete ${model.name}" else "download ${model.name}",
                tint = if(model.downloaded){
                    Color(0xFF51B955)
                }else{
                    MaterialTheme.colorScheme.onBackground
                }
            )
        }



    }
}