package com.zzz.feature_trip.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme
import com.zzz.feature_trip.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    navToCreateTrip : ()->Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {

    HomePage(
        navToCreateTrip = navToCreateTrip
    )
}

@Composable
private fun HomePage(
    navToCreateTrip : ()->Unit,
    modifier: Modifier = Modifier
) {

    Box(Modifier.fillMaxSize()){
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                CircularIconButton(
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

}

@Preview
@Composable
private fun HomePrev() {
    WanderaTheme {
        HomePage(navToCreateTrip = {})
    }
}