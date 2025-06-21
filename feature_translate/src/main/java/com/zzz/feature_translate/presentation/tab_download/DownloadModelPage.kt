package com.zzz.feature_translate.presentation.tab_download

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.ConfirmActionDialog
import com.zzz.core.presentation.dialogs.LoadingDialog
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.R
import com.zzz.feature_translate.presentation.tab_download.components.FirstTimeInstructions
import com.zzz.feature_translate.presentation.tab_download.components.TranslateModelItem
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState

@Composable
internal fun DownloadModelPage(
    state: TranslateState ,
    models: List<TranslationModel> ,
    onAction: (TranslateAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    var confirmDeleteDialog by remember { mutableStateOf(false) }
    var switch by remember { mutableStateOf(false) }

    BackHandler {

    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            VerticalSpace()
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Switch(
                    checked = switch,
                    onCheckedChange = {
                        switch = it
                    }
                )
            }

            Text(
                "Browse models" ,
                fontSize = 15.sp ,
                fontWeight = FontWeight.Bold ,
                textAlign = TextAlign.Center
            )
            LazyColumn(
                Modifier.fillMaxWidth() ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    models ,
                    key = { it.name }
                ) { model ->
                    TranslateModelItem(
                        model = model ,
                        onLongClick = { code , name ->
                            onAction(TranslateAction.ManagerAction.SetModelToDelete(code , name))
                            confirmDeleteDialog = true
                        } ,
                        onDownloadModel = { modelCode ->
                            onAction(TranslateAction.ManagerAction.DownloadModel(modelCode))
                        } ,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
        if (state.downloading) {
            LoadingDialog(
                title = "Downloading model please wait..." ,
            )
        }
        if (confirmDeleteDialog) {
            ConfirmActionDialog(
                title = "Are you sure you want to delete ${state.modelToDelete}?" ,
                actionText = "Delete" ,
                onCancel = {
                    confirmDeleteDialog = false
                    onAction(
                        TranslateAction.ManagerAction.SetModelToDelete(
                            null , null
                        )
                    )
                } ,
                onConfirm = {
                    confirmDeleteDialog = false
                    onAction(
                        TranslateAction.ManagerAction.DeleteModel
                    )
                }
            )
        }
        if (state.deleting) {
            LoadingDialog(
                title = "Deleting ${state.modelToDelete}"
            )
        }

    }

}