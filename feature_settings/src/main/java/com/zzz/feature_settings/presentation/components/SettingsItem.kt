package com.zzz.feature_settings.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.modifiers.customShadow

@Composable
internal fun SettingsSection(
    modifier: Modifier = Modifier ,
    sectionTitle : String ,
    titleFontSize: TextUnit = 18.sp ,
    titleFontWeight: FontWeight = FontWeight.Medium ,
    content : @Composable ()->Unit
) {
    Column(
        modifier.fillMaxWidth()
    ) {
        Text(
            text = sectionTitle,
            fontSize = titleFontSize,
            fontWeight = titleFontWeight
        )
        VerticalSpace(10.dp)
        content()
        VerticalSpace(10.dp)

    }
}

@Composable
internal fun SettingsItem(
    modifier: Modifier = Modifier ,
    @DrawableRes icon: Int ,
    iconTint : Color = Color.Magenta ,
    iconSize : Dp = 25.dp ,
    title: String ,
    subTitle: String? = null ,
    onClick : () -> Unit ,
    clickEnabled : Boolean = true ,
    titleFontSize: TextUnit = 16.sp ,
    titleFontWeight: FontWeight = FontWeight.Medium ,
    subTitleFontSize: TextUnit = 13.sp ,
    containerShape : Shape = RectangleShape ,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer ,
    contentColor : Color = MaterialTheme.colorScheme.onSurfaceVariant ,
    shadowColor : Color = MaterialTheme.colorScheme.onBackground ,
    shadowAlpha : Float = 0.3f ,
    shadowOffsetY : Float = 10f
) {

    Row (
        modifier
            .customShadow(
                color = shadowColor ,
                alpha = shadowAlpha ,
                offsetY = shadowOffsetY,
                borderRadius = 0.dp
            )
            .clip(containerShape)
            .fillMaxWidth()
            .background(containerColor)
            .clickable(
                enabled = clickEnabled ,
                onClick = onClick
            )
            .padding(vertical = 4.dp , horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Icon(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )
        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = contentColor
            )
            subTitle?.let{
                Text(
                    text = subTitle,
                    fontSize = subTitleFontSize,
                    color = contentColor.copy(0.8f),
                    lineHeight = TextUnit(value = subTitleFontSize.value + 2.sp.value, type = TextUnitType.Sp)
                )
            }

        }
    }

}