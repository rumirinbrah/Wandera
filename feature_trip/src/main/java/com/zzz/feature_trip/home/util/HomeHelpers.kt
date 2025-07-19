package com.zzz.feature_trip.home.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.zzz.core.presentation.modifiers.drawTicketCutout
import com.zzz.core.util.toLocalDateTime
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextInt

internal fun getDateDifference(startDate : Long, endDate : Long) : Int{
    val start = startDate.toLocalDateTime()
    val end = endDate.toLocalDateTime()
    return abs(end.dayOfMonth - start.dayOfMonth)
}

// --- TICKET CONTAINER HELPERS ---
/**
 * Used to convert the barcode to an ImageBitmap which can be drawn which isn't affected by recompositions.
 * @param height Barcode height
 * @param width Width of barcode bars
 * @param density I mean...density
 * @param barcodeColor Color of bars
 */
internal fun drawBarcodeIntoImage(
    height: Dp ,
    width: Dp ,
    density: Density ,
    barcodeColor: Color = Color.Black.copy(0.8f) ,
    barHeight: Float = 2f ,
): ImageBitmap {

    val size = with(density) {
        Size(width.toPx() , height.toPx())
    }

    val imageBitmap = ImageBitmap(size.width.toInt() , size.height.toInt())
    val drawScope = CanvasDrawScope()
    val canvas = Canvas(imageBitmap)

    drawScope.draw(
        density = density ,
        layoutDirection = LayoutDirection.Ltr ,
        canvas = canvas ,
        size = size
    ) {
        drawVerticalBarcode(
            barColor = barcodeColor ,
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
internal fun DrawScope.drawVerticalBarcode(
    barColor: Color = Color.Black.copy(0.8f) ,
    barHeight: Float = 2f ,
    drawHelperLines: Boolean = false
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
        } else if (drawHelperLines) {
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
 * @return Boolean list, true for a black bar.
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
/**
 * Draws TicketContainer decorations into an Image. Survives recompositions
 * @param size Size of parent container
 * @param density Density
 * @param endPadding Padding from the end where decorations are to be drawn
 * @return ImageBitmap
 */
internal fun drawTicketDecorationIntoImage(
    size: Size ,
    density: Density ,
    endPadding : Dp ,
    dashColor: Color = Color.Black ,
    cutoutColor : Color = Color.Black
) : ImageBitmap {

    val imageBitmap = ImageBitmap(size.width.toInt(),size.height.toInt())
    val canvas = Canvas(imageBitmap)
    val drawScope = CanvasDrawScope()

    drawScope.draw(
        density,
        layoutDirection = LayoutDirection.Ltr,
        canvas,
        size
    ){
        drawTicketDecoration(
            xOffset = size.width - endPadding.toPx(),
            dashColor,
            cutoutColor
        )
    }
    return imageBitmap
}

/**
 * Draws ticket cutout and dashed vertical line
 * @param xOffset Offset from the end where components will be drawn
 */
private fun DrawScope.drawTicketDecoration(
    xOffset : Float ,
    dashColor: Color = Color.Black ,
    cutoutColor : Color = Color.Black
){
    drawLine(
        dashColor ,
        start = Offset(xOffset , 0f) ,
        end = Offset(xOffset , size.height) ,
        strokeWidth = 5f ,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f , 15f))
    )
    drawTicketCutout(
        xOffset,
        color = cutoutColor
    )
}


