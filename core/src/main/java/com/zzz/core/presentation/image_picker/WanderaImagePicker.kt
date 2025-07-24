package com.zzz.core.presentation.image_picker

import android.Manifest
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.SheetState
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.components.rememberWanderaSheetState
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
import com.zzz.core.presentation.permission.openAppSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Custom image picker. Handles media access permissions for API 26 to 35
 * @param launcherState Used to hide picker when needed
 * @param onImagePicked Callback lambda that returns the picked image
 * @param background Background for the picker sheet
 */
@Composable
fun WanderaImagePicker(
    launcherState: LauncherImagePickerState ,
    onImagePicked: (imageUri: Uri) -> Unit ,
    modifier: Modifier = Modifier ,
    background: Color = MaterialTheme.colorScheme.surfaceContainer ,
    sheetShape: Shape = RoundedCornerShape(topEnd = 40.dp , topStart = 40.dp)
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState { 2 }

    val pickerViewModel = koinViewModel<PickerViewModel>()
    val state by pickerViewModel.state.collectAsStateWithLifecycle()

    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val deniedPermsQueue = permissionViewModel.permissionDialogQueue
    val permissionEvents = permissionViewModel.events

    /*
    val screenHeight = remember(density) {
        with(density) {
            localConfig.screenHeightDp.dp.toPx()
        }
    }
    val anchors = remember {
        DraggableAnchors {
            WanderaSheet.CLOSED at screenHeight
            WanderaSheet.HALF_EXPANDED at screenHeight * 0.3f
            WanderaSheet.EXPANDED at 0f
        }
    }
    val decay = rememberSplineBasedDecay<Float>()
    val translationY = remember {
        Animatable(
            screenHeight * 0.3f
        )
    }
    translationY.updateBounds(0f , screenHeight)
    val anchorState = rememberDraggableState { dragAmount ->
        scope.launch {
            translationY.snapTo(translationY.value + dragAmount)
        }
    }

     */

    val sheetState = rememberWanderaSheetState(SheetState.CLOSED)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.onEach { (permission , granted) ->
            println("GRANTED $granted for $permission")
            permissionViewModel.onPermissionResult(permission , granted)
        }
    }

    LaunchedEffect(launcherState.pickerVisible) {
        if (!launcherState.pickerVisible) {
            //translationY.snapTo(anchors.positionOf(WanderaSheet.CLOSED))
            sheetState.animateTo(SheetState.CLOSED)
            pickerViewModel.clearViewModel()
        } else {
            //translationY.animateTo(anchors.positionOf(WanderaSheet.HALF_EXPANDED))
            sheetState.animateTo(SheetState.HALF_EXPANDED)
            context.checkStoragePermissions(
                notGrantedBelowAndroid12 = {
                    println("Denied for Android<=12")
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    )
                    //permissionViewModel.onPermissionResult(Manifest.permission.READ_EXTERNAL_STORAGE,false)
                } ,
                notGrantedAboveAndroid13 = {
                    println("Denied for Android>=13")
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    )
                    //permissionViewModel.onPermissionResult(Manifest.permission.READ_MEDIA_IMAGES,false)
                } ,
                granted = {
                    pickerViewModel.onAction(ImagePickerActions.Load)
                    //permissionViewModel.onPermissionResult(Manifest.permission.READ_MEDIA_IMAGES,false)

                }
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            pickerViewModel.clearViewModel()

        }
    }
    //callback
    LaunchedEffect(state.selectedImage) {
        state.selectedImage?.let {
            onImagePicked(it)
            //delay(500)
            //pickerViewModel.clearViewModel()
            //launcherState.dismiss()
            //sheetState.dismiss()
        }
    }

    //permissions
    /*
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


     */
    ObserveAsEvents(permissionEvents) { event ->
        when (event) {
            UIEvents.Success -> {
                pickerViewModel.onAction(ImagePickerActions.Load)
            }

            else -> Unit
        }
    }


    Box(
        modifier
            .fillMaxSize()
            .graphicsLayer {
                //this.translationY = translationY.value
                this.translationY = sheetState.translationY.value
            }
            .background(
                background ,
                sheetShape
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .draggable(
                    state = sheetState.draggableState ,
                    orientation = Orientation.Vertical ,
                    onDragStopped = { velocity ->
                        val decayY = sheetState.calculateDecayTarget(
                            sheetState.translationY.value ,
                            velocity
                        )
                        val transY = sheetState.translationY.value
                        val halfAnchor = sheetState.getAnchorValue(SheetState.HALF_EXPANDED)
                        val fullAnchor = sheetState.getAnchorValue(SheetState.EXPANDED)
                        val closedAnchor = sheetState.getAnchorValue(SheetState.CLOSED)

                        scope.launch {
                            val targetState = when {
                                //fling up
                                decayY < (fullAnchor + halfAnchor) / 2 -> SheetState.EXPANDED
                                /*
                                fling down
                                transY < (fullAnchor + halfAnchor)/2 -> SheetState.EXPANDED
                                decayY > (halfAnchor + closedAnchor)/2 -> SheetState.CLOSED

                                 */
                                //pull down
                                transY > (halfAnchor + closedAnchor) / 2 -> SheetState.CLOSED
                                else -> SheetState.HALF_EXPANDED
                            }
                            sheetState.animateTo(targetState)
                            //HIDE SHEET
                            if (targetState == SheetState.CLOSED) {
                                launcherState.dismiss()
                                //sheetState.dismiss()
                                //pickerViewModel.clearViewModel()
                            }

                        }
                        /*
                        val decayY = decay.calculateTargetValue(
                            translationY.value,
                            velocity
                        )
                        val halfAnchor = anchors.positionOf(SheetState.HALF_EXPANDED)
                        val fullAnchor = anchors.positionOf(SheetState.EXPANDED)
                        val closedAnchor = anchors.positionOf(SheetState.CLOSED)

                        scope.launch {

                            val targetState = when{
                                decayY < (fullAnchor + halfAnchor)/2 -> SheetState.EXPANDED
                                decayY > (halfAnchor + closedAnchor)/2 -> SheetState.CLOSED
                                else -> SheetState.HALF_EXPANDED
                            }
                            translationY.animateTo(
                                anchors.positionOf(targetState)
                            )
                            if(targetState == SheetState.CLOSED){
                                launcherState.dismiss()
                                pickerViewModel.clearViewModel()
                            }

                        }

                         */
                    }
                )
        ) {
            //sheet drag thumb
            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .clip(MaterialTheme.shapes.large)
                    .width(30.dp)
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.5f))
                    .align(Alignment.CenterHorizontally)
            )

            VerticalSpace()

            PickerTabRow(
                currentTab = pagerState.currentPage ,
                onTabChange = {newTab->
                    scope.launch {
                        pagerState.animateScrollToPage(newTab)
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
                            loading = state.loading,
                            onAction = { action ->
                                pickerViewModel.onAction(action)
                            },
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

/*
enum class WanderaSheet {
    CLOSED ,
    HALF_EXPANDED ,
    EXPANDED
}
 */
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

