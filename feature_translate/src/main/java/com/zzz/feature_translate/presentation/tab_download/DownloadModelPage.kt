package com.zzz.feature_translate.presentation.tab_download

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.presentation.tab_download.components.TranslateModelItem
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DownloadModelPage(
    state: TranslateState ,
    models: List<TranslationModel> ,
    onAction: (TranslateAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var confirmDeleteDialog by remember { mutableStateOf(false) }
    var switch by remember { mutableStateOf(false) }

    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val deniedPermsQueue = permissionViewModel.permissionDialogQueue

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionViewModel.onPermissionResult(Manifest.permission.POST_NOTIFICATIONS , granted)
    }

    BackHandler {

    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = context.hasPermission(Manifest.permission.POST_NOTIFICATIONS)
            if (!granted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
            Row(
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Switch(
                    checked = switch ,
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
                        onDownloadModel = { code , name ->
                            onAction(TranslateAction.ManagerAction.DownloadModel(code,name))
                        } ,
                        modifier = Modifier.animateItem()
                    )
                }
            }
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
        deniedPermsQueue.onEach { permission ->
            PermissionDialog(
                permission = permission ,
                isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity ,
                    permission
                ),
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