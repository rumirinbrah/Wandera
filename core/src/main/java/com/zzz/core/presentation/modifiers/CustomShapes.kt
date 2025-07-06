package com.zzz.core.presentation.modifiers

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates a shape having a curve at the top and sharp bottom edges
 * @param bendAngle - Can be used to control the curve amount of shape
 */
fun Density.baldyShape(bendAngle : Dp = 60.dp) : Shape = GenericShape{ size , _->
    moveTo(0f,40.dp.toPx())
    quadraticTo(
        x1 = size.width/2,
        y1 = (-bendAngle).toPx(),
        x2= size.width,
        y2= 40.dp.toPx()
    )
    lineTo(size.width,size.height)
    lineTo(0f,size.height)
    close()
}

fun Density.trapeziumShape(displacementFraction : Int = 5) : Shape = GenericShape{size , _->
    val displacementAmount  = size.height/displacementFraction
    moveTo(0f, displacementAmount)

    lineTo(size.width,0f)
    lineTo(size.width,size.height)
    lineTo(0f , size.height - displacementAmount)

    close()
}