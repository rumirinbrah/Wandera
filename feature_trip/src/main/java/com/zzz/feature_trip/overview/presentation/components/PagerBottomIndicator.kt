package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * @author zyzz
 * Shows the count of all pager items as well as the current position in pager
 */
@Composable
internal fun PagerBottomIndicator(
    currentPage : Int,
    pageCount : Int = 5,
    modifier: Modifier = Modifier
)
{
    val rowScrollState = rememberScrollState()


    LaunchedEffect(currentPage) {
        if(pageCount<20){
            return@LaunchedEffect
        }
        when{
            currentPage==pageCount->{
                rowScrollState.scrollTo(rowScrollState.maxValue)
            }
            else->{
                rowScrollState.scrollTo(currentPage)
            }
        }
    }

    Row (
        modifier = modifier
            .horizontalScroll(rowScrollState, enabled = false),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){

        repeat(pageCount.coerceAtMost(100)){
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