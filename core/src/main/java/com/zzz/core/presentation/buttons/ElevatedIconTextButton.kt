package com.zzz.core.presentation.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author zyzz
 */
@Composable
fun ElevatedIconTextButton(
    @DrawableRes icon: Int ,
    text: String ,
    onClick: () -> Unit ,
    iconSize : Dp = 25.dp,
    enabled : Boolean = true,
    modifier: Modifier = Modifier ,
) {
    Row (
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp ,
                MaterialTheme.colorScheme.onBackground,
                RoundedCornerShape(50)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(vertical = 8.dp , horizontal = 16.dp) ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Icon(
            painter = painterResource(icon) ,
            contentDescription = text,
            modifier = Modifier.size(iconSize),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(text, color = MaterialTheme.colorScheme.onBackground)
    }
}
@Composable
fun ElevatedTextButton(
    text: String ,
    onClick: () -> Unit ,
    enabled : Boolean = true,
    contentDescription : String? = null,
    modifier: Modifier = Modifier ,
) {
    Box (
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp ,
                MaterialTheme.colorScheme.onBackground,
                RoundedCornerShape(50)
            )
            .clickable(
                onClickLabel = contentDescription,
                enabled = enabled,
                onClick = onClick
            )
            .padding(vertical = 8.dp , horizontal = 16.dp) ,
    ){
        Text(text, color = MaterialTheme.colorScheme.onBackground)
    }
}
