package com.zzz.core.presentation.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zzz.core.R

/**
 * @author zyzz
 */
@Composable
fun CircularIconButton(
    @DrawableRes icon : Int,
    contentDescription : String,
    onClick : ()->Unit,
    modifier: Modifier = Modifier,
    background : Color = MaterialTheme.colorScheme.primary,
    onBackground : Color = MaterialTheme.colorScheme.onPrimary,
    alpha : Float = 1f,
    iconSize : Dp = 25.dp,
    buttonSize : Dp = 50.dp
) {

    Box(
        modifier.clip(CircleShape)
            .size(buttonSize)
            .alpha(alpha)
            .background(background)
            .clickable {
                onClick()
            }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(icon) ,
            contentDescription = contentDescription,
            tint = onBackground,
            modifier = Modifier.size(iconSize)
        )
    }
}