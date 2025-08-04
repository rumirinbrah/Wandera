package com.zzz.feature_trip.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.modifiers.customShadow

/**
 * Home top bar with settings and create new action
 */
@Composable
internal fun HomeTopBar(
    modifier: Modifier = Modifier,
    navToThemeSettings :()->Unit,
    navToCreateTrip : ()->Unit
) {

    Column(
        modifier ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircularIconButton(
            modifier = Modifier.align(Alignment.Start),
            icon = com.zzz.core.R.drawable.settings ,
            contentDescription = "Go to app settings" ,
            onClick = {
                navToThemeSettings()
            },
            background = MaterialTheme.colorScheme.surfaceContainer,
            onBackground = MaterialTheme.colorScheme.onBackground,
            buttonSize = 40.dp,
            iconSize = 25.dp
        )
        //HEADER
        Row(
            Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                VerticalSpace(10.dp)
                Text(
                    "WELCOME," ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Traveller",
                    fontSize = 17.sp,
                )
            }
            //create new
            CircularIconButton(
                modifier = Modifier
                    .customShadow(
                        color = MaterialTheme.colorScheme.onBackground,
                        shadowRadius = 15f,
                        offsetY = 5f,
                        alpha = 0.2f
                    ),
                icon = com.zzz.core.R.drawable.add ,
                contentDescription = "Create new trip" ,
                onClick = {
                    navToCreateTrip()
                },
                iconSize = 30.dp
            )

        }
    }
}