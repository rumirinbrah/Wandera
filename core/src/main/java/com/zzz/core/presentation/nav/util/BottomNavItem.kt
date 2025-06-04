package com.zzz.core.presentation.nav.util

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

/**
 * Represents a bottom navigation bar item
 * @return Route of the screen
 */
@Composable
internal fun BottomNavItem(
    navItem: NavItem ,
    selected: Boolean = false ,
    onClick: (route: Screen) -> Unit ,
    modifier: Modifier = Modifier ,
    iconSize: Dp = 35.dp
) {

    val offset = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(selected) {
        if(!selected){
            Log.d("offsetAnimation" , "Animate to 0f for ${navItem.title} ")
            offset.animateTo(0f)
        }else{
            Log.d("offsetAnimation" , "Animate to 20f for ${navItem.title} ")
            offset.animateTo(-20f)
        }
    }

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .offset{
                    IntOffset(x=0,y=offset.value.toInt())
                }
        ) {
            Box(
                Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (!selected) {

                            onClick(navItem.route)
                        }
                    }
                    .background(
                        if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .padding(8.dp)

            ) {
                Icon(
                    modifier = Modifier.size(iconSize) ,
                    painter = painterResource(navItem.icon) ,
                    contentDescription = navItem.title ,
                    tint = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(navItem.title , fontSize = 14.sp)
    }


}
