package com.zzz.core.presentation.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a custom shadow to the component
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
            shadowRadius,
            offsetX,
            offsetY,
            color.copy(alpha).toArgb()
        )


        it.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}

fun DrawScope.drawStrikethroughLine(
    color: Color ,
    progress: Float = 1f ,
    alpha: Float = 0.5f,
    strokeWidth : Float = 10f
) {
        drawLine(
            color,
            start = Offset(
                0f ,
                size.height / 2
            ) ,
            end =  Offset(
                size.width * progress,
                size.height / 2
            ) ,
            strokeWidth = strokeWidth,
            alpha = alpha
        )
    }
fun DrawScope.drawFrameCorners(
    color: Color = Color.Gray,
    frameEdgeHeight : Dp = 30.dp,
    alpha : Float = 1f ,
    strokeWidth: Float = 15f
){
    //top left
    drawLine(
        color,
        start = Offset(0f,0f) ,
        end = Offset(frameEdgeHeight.toPx() ,0f),
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color,
        start = Offset(0f,0f) ,
        end = Offset(0f,frameEdgeHeight.toPx()),
        alpha = alpha ,
        strokeWidth = strokeWidth
    )

    //top right
    drawLine(
        color,
        start = Offset(size.width,0f) ,
        end = Offset(size.width - frameEdgeHeight.toPx() ,0f),
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color,
        start = Offset(size.width,0f) ,
        end = Offset(size.width ,frameEdgeHeight.toPx()),
        alpha = alpha ,
        strokeWidth = strokeWidth
    )

    //bottom left
    drawLine(
        color,
        start = Offset(0f , size.height) ,
        end = Offset(frameEdgeHeight.toPx() , size.height) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color,
        start = Offset(0f , size.height) ,
        end = Offset(0f , size.height-frameEdgeHeight.toPx()) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )

    //bottom right
    drawLine(
        color,
        start = Offset(size.width , size.height ) ,
        end = Offset(size.width , size.height- frameEdgeHeight.toPx()) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
    drawLine(
        color,
        start = Offset(size.width , size.height) ,
        end = Offset(size.width - frameEdgeHeight.toPx() , size.height) ,
        alpha = alpha ,
        strokeWidth = strokeWidth
    )
}

fun DrawScope.drawTicketCutout(
    centerX : Float,
    color: Color = Color.Black,
    cutoutSize : Dp = 7.dp
){
    val cutoutSizePx = cutoutSize.toPx()
    val path = Path().apply {
        moveTo(centerX - cutoutSizePx , 0f)
        lineTo(centerX , cutoutSizePx)
        lineTo(centerX + cutoutSizePx , 0f)
        close()
    }
    drawPath(path , color)
}


