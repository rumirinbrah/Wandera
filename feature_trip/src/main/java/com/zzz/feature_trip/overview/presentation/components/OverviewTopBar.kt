package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.modifiers.customShadow

@Composable
internal fun OverviewTopBar(
    tripName: String = "" ,
    onBack: () -> Unit ,
    editTrip: () -> Unit ,
    modifier: Modifier = Modifier
) {

    /*
    Box(
        modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
    ){
        CircularIconButton(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterStart),
            icon = com.zzz.core.R.drawable.arrow_back ,
            contentDescription = "Go back" ,
            background = Color.DarkGray.copy(0.4f) ,
            onBackground = Color.White,
            onClick = {
                onBack()
            }
        )
        Text(
            tripName,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 8.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }

     */

    Row(
        modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large) ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularIconButton(
            modifier = Modifier
                .padding(4.dp) ,
            icon = com.zzz.core.R.drawable.arrow_back ,
            contentDescription = "Go back" ,
            background = Color.DarkGray.copy(0.4f) ,
            onBackground = Color.White ,
            onClick = {
                onBack()
            }
        )
        Text(
            tripName ,
            fontSize = 25.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp) ,
            maxLines = 2 ,
            overflow = TextOverflow.Ellipsis ,
            textAlign = TextAlign.Center
        )


        CircularIconButton(
            modifier = Modifier
                .padding(4.dp) ,
            icon = com.zzz.core.R.drawable.delete ,
            contentDescription = "Go back" ,
            background = Color.DarkGray.copy(0.4f) ,
            onBackground = Color.White ,
            onClick = {

            }
        )



    }
}

@Composable
fun OverviewPageFab(
    collapsed : Boolean = true,
    onCollapse : (collapsed : Boolean)->Unit,
    onEdit : ()->Unit,
    onShare :()->Unit,
    onMarkAsDone : () ->Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier ,
        horizontalAlignment = Alignment.End ,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        AnimatedVisibility(!collapsed) {
            Column(
                horizontalAlignment = Alignment.End ,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                CircularIconButton(
                    modifier = Modifier
                        .customShadow(MaterialTheme.colorScheme.onBackground),
                    icon = com.zzz.core.R.drawable.edit ,
                    contentDescription = "Edit trip" ,
                    onClick = {
                        onEdit()
                    }
                )

                CircularIconButton(
                    modifier = Modifier
                        .customShadow(MaterialTheme.colorScheme.onBackground),
                    icon = com.zzz.core.R.drawable.share ,
                    contentDescription = "share trip plan with friends" ,
                    onClick = {
                        onShare()
                    }
                )

                CircularIconButton(
                    modifier = Modifier
                        .customShadow(MaterialTheme.colorScheme.onBackground),
                    icon = com.zzz.core.R.drawable.download_done ,
                    contentDescription = "mark trip as done / completed" ,
                    onClick = {
                        onMarkAsDone()
                    }
                )
            }
        }
        CircularIconButton(
            modifier = Modifier ,
            icon = com.zzz.core.R.drawable.round_more_vert_24 ,
            contentDescription = "Go back" ,
            background = MaterialTheme.colorScheme.onBackground ,
            onBackground = MaterialTheme.colorScheme.background ,
            onClick = {
                onCollapse(!collapsed)
            }
        )

    }
}