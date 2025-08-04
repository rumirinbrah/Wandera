package com.zzz.feature_trip.share.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.buttons.WanderaTextButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.core.presentation.modifiers.drawSinePath
import com.zzz.core.presentation.toast.WanderaToast
import com.zzz.core.presentation.toast.WanderaToastState
import com.zzz.feature_trip.R
import com.zzz.feature_trip.share.domain.models.ShareEvents
import com.zzz.feature_trip.share.presentation.viewmodel.ShareAction
import com.zzz.feature_trip.share.presentation.viewmodel.ShareState
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExportTripRoot(
    tripId: Long ,
    shareTripViewModel: ShareTripViewModel = koinViewModel() ,
    navToSettings : ()->Unit= {},
    navUp : () ->Unit,
    toastState : WanderaToastState,
    modifier: Modifier = Modifier
) {
    val state by shareTripViewModel.state.collectAsStateWithLifecycle()
    val events = shareTripViewModel.events
    ExportTripPage(
        tripId = tripId,
        state = state,
        events = events,
        onAction = {action->
            shareTripViewModel.onAction(action)
        },
        navToSettings = navToSettings,
        navUp = navUp,
        toastState = toastState,
        modifier = modifier
    )
}

@Composable
internal fun ExportTripPage(
    tripId : Long,
    state : ShareState,
    events : Flow<ShareEvents>,
    onAction : (action : ShareAction)->Unit,
    navToSettings : ()->Unit= {},
    navUp : () ->Unit,
    toastState: WanderaToastState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    //val shareViewModel = koinViewModel<ShareTripViewModel>()
    //val state by shareViewModel.state.collectAsStateWithLifecycle()
    //val events = shareViewModel.events

    ObserveAsEvents(events) {event->
        when(event){
            is ShareEvents.IntentGenerated -> {
                context.startActivity(event.shareIntent)
            }
        }
    }

    BackHandler {
        if(!state.inProgress){
            navUp()
        }
    }
    Box(
        //Modifier.background(appIconBackground)
    ){
//        HotAndSexyBackground(
//            Modifier.fillMaxSize()
//        )
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButtonHeader(
                actionIcon = com.zzz.core.R.drawable.arrow_back,
                onAction = navUp,
                title = null
            )
            VerticalSpace(40.dp)
            Text(
                "How sharing with Wandera works?",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(
                    R.drawable.share_link_undraw
                ),
                contentDescription = "share image illustration",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )

            VerticalSpace(5.dp)
            Text(
                text = buildAnnotatedString {
                    pushStyle(
                        SpanStyle(
                            fontSize = 18.sp,
                        )
                    )
                    append("Wandera stores all your data locally. As a result, when you share a Trip, ")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ){
                        append("your Trip's Details, Itinerary ")
                    }
                    append("will be the only things that are shared.")
                    append("\n\nNote that ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ){
                        append("Images & your Documents")
                    }
                    append(" are not shared.")

                    append("\nThe person you're sharing with can add the images later on.")
                },
                textAlign = TextAlign.Center,
            )


            Text("Trip ID $tripId")

            VerticalSpace()
            if(state.primaryButtonVisible){
                IconTextButton(
                    icon = com.zzz.core.R.drawable.share ,
                    text = "Start Share" ,
                    onClick = {
                        onAction(ShareAction.ExportTrip(tripId))
                    },
                    background = MaterialTheme.colorScheme.surfaceContainer.copy(0.5f),
                    onBackground = MaterialTheme.colorScheme.onBackground
                )
            }


            VerticalSpace(10.dp)
            if(state.inProgress){
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    strokeWidth = 3.dp,
                )
            }
            Text(
                state.progressMsg,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            VerticalSpace(10.dp)
            WanderaTextButton(
                text = "How do I import a Trip in Wandera?",
                onClick = {
                    if(!state.inProgress){
                        navToSettings()
                    }else{
                        toastState.showToast(message = "Export in progress, please wait...")
                    }
                }
            )
            VerticalSpace()

            /*
            SelectionContainer {
                state.encodedTrip?.let {
                    Text(it)
                }
            }

             */



        }
        //-------- OVERLAYS -------
        WanderaToast(
            state = toastState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@Composable
fun HotAndSexyBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val boxRandomTranslation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000,1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val boxRandomRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 70f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000,500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFFC417AB) ,Color(0xFFC425E5)),
            start = Offset(0f,0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    }

    Canvas(
        modifier
            .background(brush)
            .padding(26.dp)
    ) {
        /*
        rotate(boxRandomRotation.value){
            drawRect(
                color = Color.White,
                size = Size(100f,100f),
                topLeft = Offset(
                    x = size.width/3 ,
                    y = size.height * boxRandomTranslation.value
                )
            )
        }

         */
        drawRect(Color.Gray , size = Size(100f,100f))
        val yPosi = (1f - boxRandomTranslation.value) * size.height/2
        val sinePath = drawSinePath(
            amplitude = 50f ,
            phaseShift = 10f ,
            yPosition = yPosi ,
            step = 2,
            frequency = 5
        )
        sinePath.close()
        drawPath(sinePath , Color.Black , style = Stroke(10f))

    }
}

@Preview(showBackground = true)
@Composable
private fun ExportPrev() {
    MaterialTheme {
        Column (
            Modifier.fillMaxSize()
        ){
            //ExportTripPage(0)
        }
    }
}