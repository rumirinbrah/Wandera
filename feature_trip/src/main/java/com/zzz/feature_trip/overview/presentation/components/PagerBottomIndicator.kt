package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerBottomIndicator(
    currentPage : Int,
    pageCount : Int = 5,
    modifier: Modifier = Modifier
)
{
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        repeat(pageCount){
            Box(
                Modifier
                    .clip(CircleShape)
                    .size(
                        if(currentPage == it){
                            10.dp
                        }else{
                            5.dp
                        }
                    )
                    .background(
                        if(currentPage == it){
                            MaterialTheme.colorScheme.onBackground
                        }else{
                            MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        }
                    )
            )
        }

    }
}