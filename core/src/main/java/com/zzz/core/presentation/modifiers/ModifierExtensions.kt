package com.zzz.core.presentation.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zzz.core.util.isSdkVersionGreaterThanEqualTo
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@Stable
fun Modifier.paddingBackground(
    background: Color ,
    padding: Dp = 16.dp ,
): Modifier {
    return this
        .background(background)
        .padding(padding)
}

/**
 * Applies common dialog properties such as shape, background, padding, etc
 * @author zyzz
 */
@Stable
fun Modifier.generalDialogProperties(
    background: Color ,
    padding: Dp = 16.dp ,
): Modifier {
    return this
        .clip(
            KiteLikeShape()
        )
        .fillMaxWidth()
        .background(background)
        .padding(padding)
}

/**
 * Applies a custom shadow to the component.
 *
 * Note that this does not work on API<30.
 *
 * If you want to support those devices, use [Modifier.conditionalCustomShadow]
 * @author zyzz
 * @param color - Shadow color
 * @param alpha - Opacity
 * @param borderRadius - To control shape of the shadow
 * @param shadowRadius - Shadow spread around the component
 * @param offsetX - Shadow offset X
 * @param offsetY - Shadow offset Y
 */
@Stable
fun Modifier.customShadow(
    color: Color = Color.Black ,
    alpha: Float = 0.3f ,
    borderRadius: Dp = 45.dp ,
    shadowRadius: Float = 15f ,
    offsetX: Float = 0f ,
    offsetY: Float = 5f ,
) = drawBehind {
    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()

        frameworkPaint.color = Color.Transparent.toArgb()

        frameworkPaint.setShadowLayer(
            shadowRadius ,
            offsetX ,
            offsetY ,
            color.copy(alpha).toArgb()
        )


        it.drawRoundRect(
            0f ,
            0f ,
            size.width ,
            size.height ,
            borderRadius.toPx() ,
            borderRadius.toPx() ,
            paint
        )
    }
}

/**
 * Checks SDK version and applies custom shadow.
 *
 * For API >=30, custom shadow is applied, else Compose shadow is applied
 */
@Stable
fun Modifier.conditionalCustomShadow(
    color: Color = Color.Black ,
    alpha: Float = 0.3f ,
    borderRadius: Dp = 45.dp ,
    shadowRadius: Float = 15f ,
    offsetX: Float = 0f ,
    offsetY: Float = 5f ,
) : Modifier{
    return when{
        isSdkVersionGreaterThanEqualTo(30)->{
            customShadow(
                color,
                alpha,
                borderRadius,
                shadowRadius,
                offsetX,
                offsetY
            )
        }
        else->{
            this.shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(borderRadius),
                ambientColor = color,
                spotColor = color
            )
        }
    }
}

/**
 * Draws a strike through line in middle of any component
 * @param progress Can be used to animate line draw
 */
@Stable
fun DrawScope.drawStrikethroughLine(
    color: Color ,
    progress: Float = 1f ,
    alpha: Float = 0.5f ,
    strokeWidth: Float = 10f
) {
    drawLine(
        color ,
        start = Offset(
            0f ,
            size.height / 2
        ) ,
        end = Offset(
            size.width * progress ,
            size.height / 2
        ) ,
        strokeWidth = strokeWidth ,
        alpha = alpha
    )
}

@Stable
fun DrawScope.drawFrameCorners(
    color: Color = Color.Gray ,
    frameEdgeHeight: Dp = 30.dp ,
    alpha: Float = 1f ,
    strokeWidth: Float = 15f
) {
    //top left
    drawLine(
        color ,
        start = Offset(0f , 0f) ,
        end = Offset(frameEdgeHeight.toPx() , 0f) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color ,
        start = Offset(0f , 0f) ,
        end = Offset(0f , frameEdgeHeight.toPx()) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )

    //top right
    drawLine(
        color ,
        start = Offset(size.width , 0f) ,
        end = Offset(size.width - frameEdgeHeight.toPx() , 0f) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color ,
        start = Offset(size.width , 0f) ,
        end = Offset(size.width , frameEdgeHeight.toPx()) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )

    //bottom left
    drawLine(
        color ,
        start = Offset(0f , size.height) ,
        end = Offset(frameEdgeHeight.toPx() , size.height) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color ,
        start = Offset(0f , size.height) ,
        end = Offset(0f , size.height - frameEdgeHeight.toPx()) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )

    //bottom right
    drawLine(
        color ,
        start = Offset(size.width , size.height) ,
        end = Offset(size.width , size.height - frameEdgeHeight.toPx()) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color ,
        start = Offset(size.width , size.height) ,
        end = Offset(size.width - frameEdgeHeight.toPx() , size.height) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
}

@Stable
fun DrawScope.drawTicketCutout(
    centerX: Float ,
    color: Color = Color.Black ,
    cutoutSize: Dp = 7.dp
) {
    val cutoutSizePx = cutoutSize.toPx()
    val path = Path().apply {
        moveTo(centerX - cutoutSizePx , 0f)
        lineTo(centerX , cutoutSizePx)
        lineTo(centerX + cutoutSizePx , 0f)
        close()
    }
    drawPath(path , color)
}

/**
 * Sine path
 */
fun DrawScope.drawSinePath(
    frequency: Int = 2 ,
    amplitude: Float ,
    phaseShift: Float ,
    yPosition: Float ,
    step: Int
): Path {
    val path = Path()
    for (x in 0..size.width.toInt() + step step step) {
        val y = yPosition + amplitude * sin((x * frequency * Math.PI) / size.width + phaseShift)
        if (path.isEmpty) {
            path.moveTo(x.toFloat() , max(0f , min(y.toFloat() , size.height)))
        } else {
            path.lineTo(x.toFloat() , max(0f , min(y.toFloat() , size.height)))
        }
    }
    return path
}
fun DrawScope.drawTopCurve(
    startAngle : Float = 180f,
    sweepAngle :Float = 90f,
    progress: Float = 1f,
    color: Color = Color.Black,
    strokeWidth: Float = 10f
){
    drawArc(
        color = color,
        style = Stroke(strokeWidth),
        startAngle = startAngle,
        sweepAngle = sweepAngle*progress,
        useCenter = false
    )
}

@Preview(showBackground = true)
@Composable
private fun Curve() {
    MaterialTheme {

    }
}




