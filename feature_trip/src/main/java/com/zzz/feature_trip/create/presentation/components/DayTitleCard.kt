package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayTitleCard(
    title : String,
    alpha : Float = 0.4f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.background(Color.Gray.copy(alpha), Shapes().large)
            .padding(16.dp)
    ){
        Text(
            title,
            fontSize = 30.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
    }
}