package com.zzz.feature_trip.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.DateHeader
import com.zzz.core.presentation.modifiers.customShadow
import com.zzz.data.trip.TripWithDays
import com.zzz.feature_trip.R
import com.zzz.feature_trip.home.util.getDateDifference
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun TripItem(
    tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier ,
) {

    val density = LocalDensity.current
    val frameColor = MaterialTheme.colorScheme.onBackground

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
            Column {
                Text(
                    trip.tripName ,
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.Bold
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
fun TicketLikeTripItem(
    tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier,
    onBackground : Color = Color.Black
) {
    val density = LocalDensity.current
    val innerRowContainerHeight = remember { mutableStateOf(60.dp) }

    val arrowComponentWidth = remember {
        mutableStateOf(0.dp)
    }

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
            .drawWithContent {
                drawContent()
                val xOffset = size.width - arrowComponentWidth.value.toPx()
                drawLine(
                    onBackground ,
                    start = Offset(xOffset , 0f) ,
                    end = Offset(xOffset , size.height) ,
                    strokeWidth = 5f ,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f , 15f))
                )
            }

    ) {
        //--- HEADER ---
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 8.dp) ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "BOARDING PASS" ,
                modifier = Modifier ,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Icon(
                painter = painterResource(
                    R.drawable.bus,
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary.copy(0.7f),
                modifier = Modifier.size(25.dp)
            )
        }

        //inner row
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(Color(0xFFE5E5E5))
            ,
            horizontalArrangement = Arrangement.spacedBy(4.dp) ,

            ) {

            //--- BARCODE ---
            VerticalBarCode(
                height = innerRowContainerHeight.value,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
                color = onBackground
            )

            Column(
                Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        with(density){
                            innerRowContainerHeight.value = it.size.height.toDp().coerceAtMost(80.dp)
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        trip.tripName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = onBackground
                    )
                    // Color(0xFFFFEAEA)
                    DateComponent(
                        background = Color.White.copy(0.7f),
                        onBackground = Color.Black,
                        startDate = trip.startDate ,
                        endDate = trip.endDate,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 15.sp,
                                color = onBackground
                            )
                        ){
                            append("Duration\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ){
                            val duration = getDateDifference(trip.startDate,trip.endDate)
                            append("$duration Days")
                        }
                    },
                    lineHeight = 16.sp
                )

                if (dayImages.any { it != null }) {
                    OverlappedImagesRow(
                        images = dayImages ,
                        imageSize = 50.dp
                    )
                }

//                DateComponent(
//                    startDate = trip.startDate ,
//                    endDate = trip.endDate
//                )
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
                        .size(35.dp),
                    tint = onBackground
                )
            }
        }
    }

}
@Composable
fun VerticalBarCode(
    width: Dp = 15.dp ,
    height : Dp = 60.dp ,
    color : Color = Color.Black,
    barHeight : Float = 2f ,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val barcodeImage = remember(height) {
        drawBarcodeToBitmap(
            width = width,
            height = height,
            density = density,
            barHeight = barHeight,
            barcodeColor = color.copy(0.7f)
        )
    }

    Canvas(
        modifier
            .width(width)
            .height(height)
    ){
        drawImage(barcodeImage)
    }
}

@Composable
fun TicketLikeTripItem(
    //tripWithDays: TripWithDays ,
    onClick: (tripId: Long) -> Unit ,
    modifier: Modifier = Modifier,
    onBackground : Color = MaterialTheme.colorScheme.onBackground
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
            }

    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color(0xFFE32121))
                .padding(start = 8.dp) ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "BOARDING PASS" ,
                modifier = Modifier ,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                painter = painterResource(
                    R.drawable.flight,
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
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
                    .padding(8.dp),
//                    .background(Color.Gray),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "JAPAN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    DateComponent(
                        background = Color(0xFFFFEAEA) ,
                        startDate =System.currentTimeMillis() ,
                        endDate = System.currentTimeMillis(),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 15.sp
                            )
                        ){
                            append("Duration\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE32121)
                            )
                        ){
                            append("7 Days")
                        }
                    },
                    lineHeight = 16.sp
                )

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


/**
 * Used to convert the barcode to an ImageBitmap which can be drawn which isn't affected by recompositions.
 * @param height Barcode height
 * @param width Width of barcode bars
 * @param density I mean...density
 * @param barcodeColor Color of bars
 */
fun drawBarcodeToBitmap(
    height : Dp,
    width : Dp,
    density: Density,
    barcodeColor: Color = Color.Black.copy(0.8f),
    barHeight: Float = 2f,
) : ImageBitmap{

    val size = with(density){
        Size(width.toPx(), height.toPx())
    }

    val imageBitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val drawScope = CanvasDrawScope()
    val canvas = Canvas(imageBitmap)

    drawScope.draw(
        density = density ,
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size
    ){
        drawVerticalBarcode(
            barColor = barcodeColor,
            barHeight = barHeight
        )
    }
    return imageBitmap
}
/**
 * Draws a vertical barcode
 * @param barColor Color of each bar in the barcode
 * @param barHeight Stroke/height of each bar
 * @param drawHelperLines To draw whitespaces too
 */
fun DrawScope.drawVerticalBarcode(
    barColor: Color = Color.Black.copy(0.8f) ,
    barHeight: Float = 2f ,
    drawHelperLines : Boolean = false
) {
    val numBars = (size.height / barHeight).toInt()
    val barList = generateBarPattern(numBars)
    var currentY = 0f

    for (i in 0..numBars) {
        if (currentY > size.height) {
            return
        }
        val isBar = try {
            barList[i]

        } catch (e: Exception) {
            false
        }
        val randomBarHeight = (barHeight + getRandomInt())

        if (isBar) {
            drawRect(
                barColor ,
                topLeft = Offset(0f , currentY + getRandomInt()) ,
                size = Size(size.width , randomBarHeight)
            )
        } else if(drawHelperLines) {
            //for testing
            drawRect(
                Color.Red ,
                topLeft = Offset(0f , currentY + getRandomInt()) ,
                size = Size(size.width , randomBarHeight)
            )
        }
        currentY += randomBarHeight
    }

}

/**
 * Helper
 */
private fun getRandomInt(range: IntRange = (0..4)): Int {
    return Random.nextInt(range)
}

/**
 * Generates a boolean List that decides whether the point should be a bar or not.
 * @param numBars Number of bars drawable in the height of component
 * @param barProb Higher probability for more black bars than whitespaces
 * @return Boolean list, true for a black bar
 */
private fun generateBarPattern(
    numBars: Int ,
    barProb: Float = 0.7f
): List<Boolean> {
    return List<Boolean>(numBars) {
        isBar(barProb)
    }
}

/**
 * Helper function for random boolean generation
 */
private fun isBar(probability: Float = 0.5f): Boolean {
    return Random.nextFloat() < probability
}

@Preview
@Composable
private fun TripItemPRev() {

    MaterialTheme {
        Box(
            Modifier.fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ){
            TicketLikeTripItem(
                onClick = {}
            )
        }
    }
}