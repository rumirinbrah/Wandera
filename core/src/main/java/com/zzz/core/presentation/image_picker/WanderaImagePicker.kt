package com.zzz.core.presentation.image_picker

import android.Manifest
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.VerticalSpace
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WanderaImagePicker(
    launcherState: LauncherImagePickerState ,
    onImagePicked: (imageUri: Uri) -> Unit ,
    modifier: Modifier = Modifier ,
    background: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val density = LocalDensity.current
    val localConfig = LocalConfiguration.current

    val pagerState = rememberPagerState { 2 }

    val pickerViewModel = koinViewModel<PickerViewModel>()
    val state by pickerViewModel.state.collectAsStateWithLifecycle()

    val permissionViewModel = koinViewModel<PermissionViewModel>()
    val deniedPermsQueue = permissionViewModel.permissionDialogQueue
    val permissionEvents = permissionViewModel.events

    val screenHeight = remember(density) {
        with(density) {
            localConfig.screenHeightDp.dp.toPx()
        }
    }
    val anchors = remember {
        DraggableAnchors {
            SheetState.CLOSED at screenHeight
            SheetState.HALF_EXPANDED at screenHeight * 0.3f
            SheetState.EXPANDED at 0f
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
            translationY.snapTo(anchors.positionOf(SheetState.CLOSED))
            pickerViewModel.clearViewModel()
        }else{
            translationY.animateTo(anchors.positionOf(SheetState.HALF_EXPANDED))
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
    }
    LaunchedEffect(state.selectedImage) {
        state.selectedImage?.let {
            onImagePicked(it)
            delay(500)
            pickerViewModel.clearViewModel()
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
                this.translationY = translationY.value
            }
            .background(
                background ,
                RoundedCornerShape(topEnd = 40.dp , topStart = 40.dp)
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .draggable(
                    state = anchorState ,
                    orientation = Orientation.Vertical ,
                    onDragStopped = {velocity->
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



                    }
                )
        ) {
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
                                onImagePicked(it)
                                launcherState.dismiss()
                                pickerViewModel.clearViewModel()
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

enum class SheetState {
    CLOSED ,
    HALF_EXPANDED ,
    EXPANDED
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

