package com.zzz.core.presentation.headers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButtonHeader(
    @DrawableRes actionIcon: Int ,
    actionDescription: String? = null ,
    title : String ,
    fontSize : TextUnit = 20.sp,
    fontWeight : FontWeight = FontWeight.Bold,
    onAction: () -> Unit ,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxWidth()
    ) {
        IconButton(
            onAction,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(actionIcon) ,
                contentDescription = actionDescription ,
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            title ,
            modifier = Modifier.align(Alignment.Center),
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}