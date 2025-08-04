package com.zzz.core.presentation.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
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
//------------- KITE -------------
fun KiteLikeShape(
    topStart : Dp = 0.dp,
    topEnd : Dp = 40.dp,
    bottomStart : Dp = 40.dp,
    bottomEnd : Dp = 0.dp,
) : Shape{
    return RoundedCornerShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd
    )
}
/*
fun Density.homeTripItemShape(
    yBend : Dp = 15.dp,
    xBend : Dp = 15.dp,
) : Shape = GenericShape{size, _ ->
    moveTo(0f,0f)

    lineTo(size.width/2 , yBend.toPx())
    lineTo(size.width , 0f)

    lineTo(size.width-xBend.toPx() , size.height/2)
    lineTo(size.width , size.height)

    lineTo(size.width/2 , size.height-yBend.toPx())
    lineTo(0f , size.height)
    close()
}

fun Density.homeTripItemContainer(

) : Shape = GenericShape{size, _ ->
    moveTo(0f,0f)

    close()

}

 */

@Preview(showBackground = true)
@Composable
private fun WanderaShapesPrev() {
    MaterialTheme {
        val density = LocalDensity.current

        Box(
            Modifier.fillMaxWidth()
                .height(150.dp)
                .drawBehind {
//                    val innerBox = homeTripItemContainer().createOutline(size, layoutDirection,density)
//                    drawOutline(
//                        innerBox, Color.Gray
//                    )
                    drawFrameCorners(color = Color.Black)
                } ,
            contentAlignment = Alignment.Center
        ){
            Text("This is a box", color = Color.White)
        }
    }
}