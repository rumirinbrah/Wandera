package com.zzz.feature_translate.presentation.tab_download

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.ConfirmActionDialog
import com.zzz.core.presentation.dialogs.LoadingDialog
import com.zzz.core.presentation.permission.PermissionDialog
import com.zzz.core.presentation.permission.PermissionViewModel
import com.zzz.core.presentation.permission.hasPermission
import com.zzz.core.presentation.permission.openAppSettings
import com.zzz.feature_translate.presentation.tab_download.components.FilterButton
import com.zzz.feature_translate.presentation.tab_download.components.TranslateModelItem
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DownloadModelPage(
    state: TranslateState ,
    onAction: (TranslateAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val deniedPermsQueue = permissionViewModel.permissionDialogQueue

    var confirmDeleteDialog by remember { mutableStateOf(false) }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionViewModel.onPermissionResult(Manifest.permission.POST_NOTIFICATIONS , granted)
    }


    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = context.hasPermission(Manifest.permission.POST_NOTIFICATIONS)
            if (!granted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(state.downloadsFilter) {
        if(!state.downloadsFilter){
            delay(500)
            lazyListState.animateScrollToItem(lazyListState.layoutInfo.viewportStartOffset)
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        //downloading sticky indictor
        AnimatedVisibility(
            state.downloading ,
            enter = slideInVertically(
                tween(1000) ,
                initialOffsetY = {
                    -it
                } ,
            ) ,
            exit = slideOutVertically(
                tween(1000) ,
                targetOffsetY = {
                    -it
                }
            ) ,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clip(RectangleShape) ,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(0.8f)) ,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Download in progress..." ,
                    fontSize = 13.sp ,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            //----- HEADER -----
            VerticalSpace(20.dp)
            Text(
                "Browse models" ,
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                textAlign = TextAlign.Center
            )
            //----- FILTER -----
            VerticalSpace(10.dp)
            FilterButton(
                text = "Downloaded" ,
                onClick = {
                    onAction(TranslateAction.ManagerAction.FilterByDownloads(it))
                } ,
                filtered = state.downloadsFilter
            )
            VerticalSpace(10.dp)

            LazyColumn(
                Modifier.fillMaxWidth() ,
                verticalArrangement = Arrangement.spacedBy(8.dp) ,
                state = lazyListState
            ) {

                items(
                    state.filteredModels,//models ,
                    key = { it.name }
                ) { model ->
                    TranslateModelItem(
                        model = model ,
                        onLongClick = { code , name ->
                            onAction(TranslateAction.ManagerAction.SetModelToDelete(code , name))
                            confirmDeleteDialog = true
                        } ,
                        onDownloadModel = { code , name ->
                            onAction(TranslateAction.ManagerAction.DownloadModel(code , name))
                        } ,
                        modifier = Modifier.animateItem()
                    )
                }
                item {
                    VerticalSpace(70.dp)
                }
            }
        }
        when{
            confirmDeleteDialog->{
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
            state.deleting->{
                LoadingDialog(
                    title = "Deleting ${state.modelToDelete}"
                )
            }
            state.cellularDataDownloadDialog->{
                ConfirmActionDialog(
                    title = "You're not connected to WIFI, do you want to download using mobile data?",
                    actionText = "Download",
                    onConfirm = {
                        onAction(TranslateAction.ManagerAction.DownloadModelWithCellularData)
                    },
                    onCancel = {
                        onAction(TranslateAction.ManagerAction.DismissCellularDownloadDialog)
                    }
                )
            }


        }
        deniedPermsQueue.onEach { permission ->
            PermissionDialog(
                permission = permission ,
                isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity ,
                    permission
                ) ,
                onDismiss = {
                    permissionViewModel.onDismiss()
                } ,
                onOkClick = {
                    permissionViewModel.onDismiss()
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } ,
                onGoToSettings = {
                    context.openAppSettings()
                }
            )
        }

    }

}