package com.zzz.feature_translate.presentation.tab_download

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.LoadingDialog
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.R
import com.zzz.feature_translate.presentation.tab_download.components.FirstTimeInstructions
import com.zzz.feature_translate.presentation.tab_download.components.TranslateModelItem
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState

@Composable
internal fun DownloadModelPage(
    state : TranslateState,
    models : List<TranslationModel>,
    onAction : (TranslateAction)->Unit,
    modifier: Modifier = Modifier
) {


    BackHandler {

    }

    Box(
        Modifier.fillMaxSize()
    ){
        Column(
            modifier.fillMaxSize()
                .padding(16.dp)
        ){

            VerticalSpace()
            Text(
                "Browse models",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            LazyColumn(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    models,
                    key = {it.name}
                ){model->
                    TranslateModelItem(
                        model = model,
                        onLongClick = {code, name->

                        },
                        onDownloadModel = {modelCode->
                            onAction(TranslateAction.ManagerAction.DownloadModel(modelCode))
                        },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
        if(state.downloading){
            LoadingDialog(
                title = "Downloading model please wait...",
                cancelText = "Cancel",
                onCancel = {
                    //cancel download somehow idk
                }
            )
        }

    }

}