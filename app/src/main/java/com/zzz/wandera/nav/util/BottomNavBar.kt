package com.zzz.wandera.nav.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zzz.core.theme.WanderaTheme

@Composable
fun BottomNavBar(
    navController : NavHostController,
    currentRoute : Screen = Screen.HomeGraph,
    onRouteChange : (Screen)->Unit,
    onHeightCalculated : (Dp)->Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface , RoundedCornerShape(40))
            .padding(horizontal = 26.dp)
            .onGloballyPositioned {
                val height = with(density){
                    it.size.height.toDp()
                }
                onHeightCalculated(height)
            },
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bottomItems.onEach { item ->
            BottomNavItem(
                navItem = item ,
                selected = currentRoute == item.route ,
                onClick = {route->
                    //currentSelected = route
                    onRouteChange(route)
                    navController.navigate(route){
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavPrev() {
    WanderaTheme {
        //BottomNavBar()
    }
}


