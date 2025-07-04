package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.components.WanderaShapes
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.util.shareText
import com.zzz.feature_trip.R

@Composable
fun ExpensesNoteBox(
    modifier: Modifier = Modifier ,
    value: String ,
    onValueChange: (String) -> Unit ,
    onSave: () -> Unit ,
    background: Color = MaterialTheme.colorScheme.surfaceContainer ,
    onBackground: Color = MaterialTheme.colorScheme.onSurface ,
) {
    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    Box(
        modifier
            .clip(WanderaShapes.singleEdgeShape)
            .fillMaxWidth()
            .background(background)
            .padding(
                start = 4.dp ,
                end = 4.dp ,
                top = 4.dp ,
                bottom = 8.dp
            )
    ) {
        RoundedTextField(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.TopStart) ,
            value = text ,
            onValueChange = { text = it } ,
            placeholder = buildAnnotatedString {
                append("You can save your trip expenses here!\nFor ex, ")
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Breakfast : Ryan $25")
                }
            } ,
            placeholderColor = MaterialTheme.colorScheme.onBackground,
            shape = WanderaShapes.singleEdgeShape ,
            background = background ,
            onBackground = onBackground
        )
        Row(
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.spacedBy(12.dp) ,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Box(
                Modifier
                    .clip(CircleShape)
                    .clickable {
                        onSave()
                    }
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.save) ,
                    contentDescription = "Save expense note" ,
                    modifier = Modifier.size(25.dp),
                    tint = onBackground
                )
            }
            Box(
                Modifier
                    .clip(CircleShape)
                    .clickable {
                        context.shareText(text)
                    }
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.send) ,
                    contentDescription = "Share expense note" ,
                    modifier = Modifier.size(25.dp),
                    tint = onBackground
                )
            }

        }
    }

}