package com.zzz.core.presentation.components

import androidx.collection.FloatIntMap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun rememberWanderaSheetState(
    initialState: SheetState = SheetState.HALF_EXPANDED
): WanderaSheetState {

    val density = LocalDensity.current
    val localConfig = LocalConfiguration.current
    val scope = rememberCoroutineScope()

    val screenHeight = remember(density) {
        with(density) {
            localConfig.screenHeightDp.dp.toPx()
        }
    }
    //exp
    val anchors = remember {
        mapOf(
            SheetState.EXPANDED to 0f ,
            SheetState.HALF_EXPANDED to 0.3f * screenHeight ,
            SheetState.CLOSED to screenHeight
        )
    }
    val decay = rememberSplineBasedDecay<Float>()
    //exp
    val translationY = remember {
        Animatable(
            initialValue = anchors.getValue(initialState)
        )
    }
    translationY.updateBounds(0f , screenHeight)
    //exp
    val draggableState = rememberDraggableState { dragAmount ->
        scope.launch {
            translationY.snapTo(translationY.value + dragAmount)
        }
    }
    return remember {
        WanderaSheetState(
            anchors = anchors ,
            animatable = translationY ,
            decayAnimationSpec = decay ,
            draggableState = draggableState ,
        )
    }

}

class WanderaSheetState(
    private val anchors: Map<SheetState , Float> ,
    private val animatable: Animatable<Float , AnimationVector1D> ,
    val decayAnimationSpec: DecayAnimationSpec<Float> ,
    draggableState: DraggableState
) {
    val translationY: State<Float> = animatable.asState()
    val draggableState = draggableState

    suspend fun animateTo(sheetState: SheetState , animationSpec: AnimationSpec<Float> = spring()) {
        try {
            animatable.animateTo(
                anchors.getValue(sheetState) ,
                animationSpec = animationSpec
            )
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            e.printStackTrace()
        }

    }

    fun calculateDecayTarget(
        initialValue: Float ,
        initialVelocity: Float
    ): Float {
        return decayAnimationSpec.calculateTargetValue(initialValue , initialVelocity)
    }

    fun getAnchorValue(sheetState: SheetState): Float {
        return try {
            anchors.getValue(sheetState)
        } catch (e: Exception) {
            e.printStackTrace()
            0f
        }
    }


}

enum class SheetState {
    HALF_EXPANDED ,
    EXPANDED ,
    CLOSED
}

