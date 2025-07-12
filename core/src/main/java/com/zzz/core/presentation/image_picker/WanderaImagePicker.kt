package com.zzz.core.presentation.image_picker

import android.Manifest
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.events.UIEvents
import com.zzz.core.presentation.image_picker.components.PickerTabRow
import com.zzz.core.presentation.image_picker.tabs.AlbumsRoot
import com.zzz.core.presentation.image_picker.tabs.RecentImagesPage
import com.zzz.core.presentation.image_picker.util.checkStoragePermissions
import com.zzz.core.presentation.image_picker.viewmodel.ImagePickerActions
import com.zzz.core.presentation.image_picker.viewmodel.LauncherImagePickerState
import com.zzz.core.presentation.image_picker.viewmodel.PickerViewModel
import com.zzz.core.presentation.permission.PermissionDialog
import com.zzz.core.presentation.permission.PermissionViewModel
import com.zzz.core.presentation.permission.hasPermission
import com.zzz.core.presentation.permission.openAppSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun WanderaImagePicker(
    launcherState: LauncherImagePickerState ,
    onImagePicked: (imageUri: Uri) -> Unit ,
    modifier: Modifier = Modifier ,
    background: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState { 2 }

    val pickerViewModel = koinViewModel<PickerViewModel>()
    val state by pickerViewModel.state.collectAsStateWithLifecycle()

    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val deniedPermsQueue = permissionViewModel.permissionDialogQueue
    val permissionEvents = permissionViewModel.events


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.onEach { (permission , granted) ->
            println("GRANTED $granted for $permission")
            permissionViewModel.onPermissionResult(permission , granted)
        }
    }


    LaunchedEffect(state.selectedImage) {
        state.selectedImage?.let {
            onImagePicked(it)
            delay(500)
            pickerViewModel.clearViewModel()
        }
    }

    //permissions
    LaunchedEffect(Unit) {
        context.checkStoragePermissions(
            notGrantedBelowAndroid12 = {
                println("Denied for Android<=12")
                permissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            } ,
            notGrantedAboveAndroid13 = {
                println("Denied for Android>=13")
                permissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                )
            } ,
            granted = {
                pickerViewModel.onAction(ImagePickerActions.Load)
            }
        )

    }

    ObserveAsEvents(permissionEvents) {event->
        when(event){
            UIEvents.Success ->{
                pickerViewModel.onAction(ImagePickerActions.Load)
            }
            else-> Unit
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(
                background ,
                RoundedCornerShape(topEnd = 40.dp , topStart = 40.dp)
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PickerTabRow(
                currentTab = pagerState.currentPage ,
                onTabChange = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            HorizontalPager(
                pagerState ,
                modifier = Modifier.fillMaxWidth() ,
            ) { page ->
                when (page) {
                    0 -> {
                        RecentImagesPage(
                            images = state.images ,
                            loadMore = {
                                pickerViewModel.loadRecentsNextPage()
                            } ,
                            onImageSelect = {

                            } ,
                            onDismiss = {
                                launcherState.dismiss()
                                pickerViewModel.clearViewModel()
                            }
                        )
                    }

                    1 -> {
                        AlbumsRoot(
                            state = state ,
                            onAction = { action ->
                                pickerViewModel.onAction(action)
                            } ,
                            onDismiss = {
                                launcherState.dismiss()
                                pickerViewModel.clearViewModel()
                            }
                        )
                    }
                }
            }
        }
        //denied permissions dialog

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
                    permissionLauncher.launch(arrayOf(permission))
                } ,
                onGoToSettings = {
                    context.openAppSettings()
                    permissionViewModel.onDismiss()
                }
            )
        }


    }
}


/**
 * Used to launch & control Wandera image picker
 */
@Composable
fun rememberWanderaImagePicker(): LauncherImagePickerState {
    return remember { LauncherImagePickerState() }
}

@Preview
@Composable
private fun PickerPrev() {
    MaterialTheme {
        //WanderaImagePicker()
    }
}

