package com.zzz.core.presentation.modifiers

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
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