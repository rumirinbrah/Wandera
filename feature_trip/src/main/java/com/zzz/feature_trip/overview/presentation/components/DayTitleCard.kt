package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

/**
 * @author zyzz
 * Used in DayDetailsPage
 * @param alpha - Opacity of text background
 */
@Composable
internal fun DayTitleCard(
    title : String,
    fontSize : TextUnit = 30.sp,
    fontWeight : FontWeight = FontWeight.ExtraBold,
    maxLines : Int = 3,
    lineHeight : TextUnit = 32.sp,
    alpha : Float = 0.4f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.background(Color.Gray.copy(alpha), Shapes().large)
            .padding(16.dp)
    ){
        Text(
            title,
            fontSize = fontSize,
            color = Color.White,
            fontWeight = fontWeight,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            lineHeight = lineHeight,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}