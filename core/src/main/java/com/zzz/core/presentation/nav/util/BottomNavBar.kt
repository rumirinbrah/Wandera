package com.zzz.core.presentation.nav.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zzz.core.theme.WanderaTheme
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun BottomNavBar(
    //navController : NavHostController,
    modifier: Modifier = Modifier
) {
    var currentSelected by remember { mutableStateOf<Screen>(Screen.TripScreen) }



        Row(
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(40))
                .padding(horizontal = 26.dp) ,
            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomItems.onEach { item ->
                BottomNavItem(
                    navItem = item ,
                    selected = currentSelected == item.route ,
                    onClick = {
                        currentSelected = it
                    }
                )
            }
        }




}

@Preview
@Composable
private fun BottomNavPrev() {
    WanderaTheme {
        BottomNavBar()
    }
}


