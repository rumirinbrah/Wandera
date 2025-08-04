package com.zzz.feature_trip.recents.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.data.trip.TripWithDays
import com.zzz.feature_trip.R
import com.zzz.feature_trip.home.presentation.components.DateComponent
import com.zzz.feature_trip.home.presentation.components.OverlappedImagesRow
import com.zzz.feature_trip.home.presentation.components.VerticalBarCodeSvg

/**
 * Flashy ticket container
 */
@OptIn(ExperimentalComposeApi::class)
@Composable
internal fun RecentTripItem(
    tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier ,
    containerOnBackground: Color = Color.Black
) {
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    val arrowTranslation = animateFloatAsState(
        targetValue = if(isPressed.value) 30f else 0f,
        animationSpec = tween(500)
    )

    val innerRowContainerHeight = remember { mutableStateOf(60.dp) }

    val dayImages = remember(tripWithDays.days) {
        tripWithDays.days.map {
            it.image
        }
    }
    val trip = remember(tripWithDays.trip) { tripWithDays.trip }


    //--- PARENT ---
    Box(
        modifier
            .clip(CutCornerShape(4.dp))
            .fillMaxWidth()
            .clickable (
                onClick = {
                    onClick(trip.id)
                },
                interactionSource = interactionSource,
                indication = null
            )
    ) {

        //--- HEADER ---
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 8.dp) ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "BOARDING PASS" ,
                modifier = Modifier ,
                color = MaterialTheme.colorScheme.onPrimary ,
            )
            Icon(
                painter = painterResource(
                    R.drawable.bus ,
                ) ,
                contentDescription = null ,
                tint = MaterialTheme.colorScheme.onPrimary.copy(0.7f) ,
                modifier = Modifier.size(25.dp)
            )
        }

        //inner row
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(Color(0xFFEFEFEF)) ,
            horizontalArrangement = Arrangement.spacedBy(4.dp) ,

            ) {

            //--- BARCODE ---

            VerticalBarCodeSvg(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Column(
                Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        with(density) {
                            innerRowContainerHeight.value =
                                it.size.height.toDp().coerceAtMost(80.dp)
                        }
                    } ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //--- TITLE & DATE
                Row(
                    Modifier.fillMaxWidth() ,
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        trip.tripName ,
                        fontWeight = FontWeight.Bold ,
                        fontSize = 20.sp ,
                        color = containerOnBackground.copy(0.8f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(
                                1f ,
                                fill = false
                            )
                            .padding(end = 4.dp)
                    )
                    /*
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 15.sp ,
                                    color = containerOnBackground
                                )
                            ) {
                                append("Duration\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 13.sp ,
                                    fontWeight = FontWeight.Bold ,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                val duration = getDateDifference(trip.startDate , trip.endDate)
                                append("$duration Days")
                            }
                        } ,
                        lineHeight = 16.sp
                    )

                     */
                    DateComponent(
                        background = Color.White.copy(0.7f) ,
                        onBackground = Color.Black ,
                        startDate = trip.startDate ,
                        endDate = trip.endDate ,
                        fontWeight = FontWeight.Bold ,

                        )
                    // Color(0xFFFFEAEA)

                }

                VerticalSpace(5.dp)
                //--- DURATION ---

                if (dayImages.any { it != null }) {
                    OverlappedImagesRow(
                        images = dayImages ,
                        imageSize = 50.dp
                    )
                }
            }
            //--- NAV ICON---
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp)
                    .graphicsLayer {
//                        rotationZ = -arrowRotation.value
                        translationX = arrowTranslation.value
                    }
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.arrow_right_round) ,
                    contentDescription = null ,
                    modifier = Modifier
                        .size(35.dp) ,
                    tint = containerOnBackground
                )
            }
        }
        TicketStamp(
            modifier = Modifier.align(Alignment.BottomCenter)
        )

    }

}

/**
 * Simple ticket stamp for finished trips
 */
@Composable
private fun TicketStamp(
    stampColor : Color = Color(0xEB293BA1) ,
    stampTextColor : Color = Color(0xF2212D75) ,
    background : Color = Color(0xFFEFEFEF),
    modifier: Modifier = Modifier
) {
    val containerSize = remember { mutableStateOf(50.dp) }
    val density = LocalDensity.current

    Box(
        modifier.clip(CircleShape)
            .background(background)
            .padding(4.dp)
            .size(containerSize.value)
            .drawWithContent {
                drawContent()
                drawCircle(
                    stampColor,
                    radius = size.width/2,
                    style = Stroke(5f)
                )
                drawCircle(
                    stampColor.copy(0.8f),
                    radius = size.width/3,
                    style = Stroke(5f)
                )
            },
        contentAlignment = Alignment.Center
    ){
        Box(
            Modifier
                .onGloballyPositioned {
                    containerSize.value = with(density){
                        val actualWidth = it.size.width.toDp()
                        actualWidth*2
                    }
                }
                .rotate(25f),
            contentAlignment = Alignment.Center
        ){
            Text(
                "DONE" ,
                fontSize = 14.sp ,
                fontWeight = FontWeight.Bold,
                color = stampTextColor,
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StampPRev() {
    MaterialTheme {
        TicketStamp()
    }
}