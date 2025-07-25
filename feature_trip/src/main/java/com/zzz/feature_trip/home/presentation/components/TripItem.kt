package com.zzz.feature_trip.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.modifiers.drawTicketCutout
import com.zzz.data.trip.TripWithDays
import com.zzz.feature_trip.R
import com.zzz.feature_trip.home.util.drawBarcodeIntoImage
import com.zzz.feature_trip.home.util.drawTicketDecorationIntoImage
import com.zzz.feature_trip.home.util.drawVerticalBarcode
import com.zzz.feature_trip.home.util.getDateDifference

@Composable
internal fun TripItemRoot(
    tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    ticketLikeContainer : Boolean = true,
    modifier: Modifier = Modifier
) {
    when{
        ticketLikeContainer->{
            TicketLikeTripItem(
                tripWithDays = tripWithDays,
                onClick = onClick,
                modifier = modifier
            )
        }
        else->{
            TripItem(
                tripWithDays = tripWithDays,
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}

@Composable
internal fun TripItem(
    tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier ,
) {


    val dayImages = remember(tripWithDays.days) {
        tripWithDays.days.map {
            it.image
        }
    }
    val trip = remember(tripWithDays.trip) { tripWithDays.trip }
    var duration by remember(trip.startDate) { mutableStateOf("Duration...") }

    LaunchedEffect(Unit) {
        val diff = getDateDifference(trip.startDate , trip.endDate)
        duration = "Duration $diff days"
    }
    Box(
        modifier
            .clip(RoundedCornerShape(15))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(
                onClick = {
                    onClick(trip.id)
                }
            )
            .padding(22.dp)

    ) {
        Row(
            Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    trip.tripName ,
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    duration ,
                    fontSize = 14.sp ,
                )
                VerticalSpace(5.dp)
                if (dayImages.any { it != null }) {
                    OverlappedImagesRow(
                        images = dayImages ,
                        imageSize = 50.dp
                    )
                }
                VerticalSpace(10.dp)
                DateComponent(
                    startDate = trip.startDate ,
                    endDate = trip.endDate
                )

            }

            Icon(
                painter = painterResource(com.zzz.core.R.drawable.arrow_right_round) ,
                contentDescription = null ,
                modifier = Modifier.size(35.dp)
            )

        }


    }
}

@Composable
internal fun TicketLikeTripItem(
    tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier ,
    containerOnBackground: Color = Color.Black
) {
    val density = LocalDensity.current
    val innerRowContainerHeight = remember { mutableStateOf(60.dp) }

    val arrowComponentWidth = remember {
        mutableStateOf(0.dp)
    }
    val ticketContainerSize = remember { mutableStateOf(Size(10f,10f)) }

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
            .clickable {
                onClick(trip.id)
            }
//            .drawWithContent {
//                val xOffset = size.width - arrowComponentWidth.value.toPx()
//                drawContent()
//                drawTicketDecoration(
//                    xOffset,
//                    cutoutColor = background
//                )
//            }
            .onGloballyPositioned {
                ticketContainerSize.value = it.size.toSize()
            }

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
            VerticalBarCode(
                height = innerRowContainerHeight.value ,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp) ,
                color = containerOnBackground
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
                    // Color(0xFFFFEAEA)

                }

                VerticalSpace(5.dp)
                //--- DURATION ---
                DateComponent(
                    background = Color.White.copy(0.7f) ,
                    onBackground = Color.Black ,
                    startDate = trip.startDate ,
                    endDate = trip.endDate ,
                    fontWeight = FontWeight.Bold ,

                    )
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
                    .onGloballyPositioned {
                        with(density) {
                            arrowComponentWidth.value = it.size.width.toDp()
                        }
                    }
                    .padding(16.dp)
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
        TicketDecorations(
            size = ticketContainerSize.value,
            endPadding = arrowComponentWidth.value
        )
    }

}

/**
 * Uses  custom barcode drawn into an ImageBitmap
 */
@Composable
internal fun VerticalBarCode(
    width: Dp = 15.dp ,
    height: Dp = 60.dp ,
    color: Color = Color.Black ,
    barHeight: Float = 2f ,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val barcodeImage = remember(height) {
        drawBarcodeIntoImage(
            width = width ,
            height = height ,
            density = density ,
            barHeight = barHeight ,
            barcodeColor = color.copy(0.7f)
        )
    }

    Canvas(
        modifier
            .width(width)
            .height(height)
    ) {
        drawImage(barcodeImage)
    }
}

@Composable
internal fun TicketDecorations(
    size: Size ,
    endPadding: Dp ,
    cutoutColor : Color = MaterialTheme.colorScheme.background ,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val decorationImage = remember (size,endPadding){
        drawTicketDecorationIntoImage(
            density = density,
            endPadding = endPadding,
            size = size,
            cutoutColor = cutoutColor
        )
    }
    val width = remember(size) {
        with(density){
            size.width.toDp()
        }
    }
    val height = remember(size) {
        with(density){
            size.height.toDp()
        }
    }
    Canvas(
        modifier
            .width(width)
            .height(height)
    ) {
        drawImage(decorationImage)
    }
}

/**
 * For PREV
 */
@Composable
private fun TicketLikeTripItem(
    //tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier ,
    onBackground: Color = MaterialTheme.colorScheme.onBackground
) {
    val density = LocalDensity.current
    val arrowComponentWidth = remember {
        mutableStateOf(0.dp)
    }

    //--- PARENT ---
    Box(
        Modifier
            .clip(CutCornerShape(4.dp))
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                val xOffset = size.width - arrowComponentWidth.value.toPx()
                drawLine(
                    onBackground ,
                    start = Offset(xOffset , 0f) ,
                    end = Offset(xOffset , size.height) ,
                    strokeWidth = 6f ,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f , 10f))
                )

                drawTicketCutout(xOffset)
            }

    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color(0xFFE32121))
                .padding(start = 8.dp) ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "BOARDING PASS" ,
                modifier = Modifier ,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                painter = painterResource(
                    R.drawable.flight ,
                ) ,
                contentDescription = null ,
                tint = MaterialTheme.colorScheme.onPrimary ,
                modifier = Modifier.size(25.dp)
            )
        }

        //inner row
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer) ,
            horizontalArrangement = Arrangement.spacedBy(4.dp) ,

            ) {

            //--- BARCODE ---
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .heightIn(min = 60.dp , max = 80.dp)
                    .fillMaxHeight()
                    .width(15.dp)
                    .drawBehind {
                        drawVerticalBarcode()
                    }
            )


            Column(
                Modifier
                    .weight(1f)
                    .padding(8.dp) ,
//                    .background(Color.Gray),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth() ,
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "JAPAN" ,
                        fontWeight = FontWeight.Bold ,
                        fontSize = 20.sp ,
                        modifier = Modifier.weight(1f)
                    )
                    DateComponent(
                        background = Color(0xFFFFEAEA) ,
                        startDate = System.currentTimeMillis() ,
                        endDate = System.currentTimeMillis() ,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 15.sp
                                )
                            ) {
                                append("Duration\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 13.sp ,
                                    fontWeight = FontWeight.Bold ,
                                    color = Color(0xFFE32121)
                                )
                            ) {
                                append("7 Days")
                            }
                        } ,
                        lineHeight = 16.sp
                    )
                    Box(
                        Modifier
                            .border(
                                1.dp ,
                                MaterialTheme.colorScheme.primary ,
                                MaterialTheme.shapes.large
                            )
                            .padding(4.dp)
                    ){
                        Text(
                            "Upcoming in 2 Days",
                            fontSize = 13.sp
                        )
                    }
                }


            }


            //--- NAV ICON---
            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .onGloballyPositioned {
                        with(density) {
                            arrowComponentWidth.value = it.size.width.toDp()
                        }
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.arrow_right_round) ,
                    contentDescription = null ,
                    modifier = Modifier
                        .size(35.dp)
                )
            }
        }
    }

}


@Preview
@Composable
private fun TripItemPRev() {

    MaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black) ,
            contentAlignment = Alignment.Center
        ) {
            TicketLikeTripItem(
                onClick = {}
            )
        }
    }
}