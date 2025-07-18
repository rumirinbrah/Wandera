package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.WanderaShapes
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.WanderaTheme
import com.zzz.core.util.shareText

@Composable
internal fun ExpensesNoteBox(
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
            .animateContentSize(
                animationSpec = tween(
                    800 ,
                    300
                )
            )
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
            placeholderColor = MaterialTheme.colorScheme.onBackground ,
            shape = WanderaShapes.singleEdgeShape ,
            background = background ,
            onBackground = onBackground
        )

        //action buttons
        AnimatedVisibility(
            text.isNotBlank() ,
            modifier = Modifier.align(Alignment.BottomEnd) ,
            enter = slideInVertically(
                animationSpec = tween(500 , 300) ,
                initialOffsetY = {
                    it * 2
                }
            ) ,
            exit = slideOutVertically(
                animationSpec = tween(500 , 300) ,
                targetOffsetY = {
                    it * 2
                }
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.spacedBy(12.dp) ,
                //modifier = Modifier.align(Alignment.BottomEnd)
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
                        modifier = Modifier.size(25.dp) ,
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
                        modifier = Modifier.size(25.dp) ,
                        tint = onBackground
                    )
                }

            }
        }

    }

}

@Composable
internal fun BookLikeTextField(
    modifier: Modifier = Modifier ,
    value: String ,
    onValueChange: (String) -> Unit ,
    onSave: () -> Unit ,
    background: Color = MaterialTheme.colorScheme.surfaceContainer ,
    onBackground: Color = MaterialTheme.colorScheme.onSurface ,
) {
    val context = LocalContext.current
    val bgColor = remember { Color(0xFFFFF3A2) }
    val lineHeight = remember { 20.sp }
    val ripple = rememberRipple(color = Color.DarkGray)



    Box(
        modifier
            .fillMaxWidth()
            .background(bgColor)
            .animateContentSize(
                animationSpec = tween(
                    400 ,
                    100
                )
            )
            .drawWithContent {
                val numLines = (size.height / lineHeight.toPx()).toInt()
                for (i in 1..numLines) {
                    val yPosi = i * lineHeight.toPx()
                    drawLine(
                        Color.Black ,
                        start = Offset(0f , yPosi) ,
                        end = Offset(size.width , yPosi) ,
                        strokeWidth = 1.3f
                    )
                }
                drawLine(
                    Color.Red ,
                    start = Offset(20.dp.toPx() , 0f) ,
                    end = Offset(20.dp.toPx() , size.height) ,
                    strokeWidth = 1.5f
                )
                drawContent()
            }
            .padding(start = 40.dp , top = 20.dp)
    ) {
        //Hello there
        //this is a notes app
        //with some retro style
        BasicTextField(
            value = value ,
            onValueChange = onValueChange ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.TopStart) ,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 15.sp ,
                lineHeight = lineHeight ,
                fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Default
            ),
            keyboardActions = KeyboardActions(

            )
        )
        if(value.isBlank()){
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color.Black,
                            fontSize = 15.sp ,
                        )
                    ){
                        append("You can save some notes here!\nFor ex, ")
                    }
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 15.sp ,
                        )
                    ) {
                        append("Breakfast : Ryan $25")
                    }
                },
                lineHeight = lineHeight,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )
        }



        //action buttons

        Row(
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.spacedBy(12.dp) ,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(0.8f))
                    .clickable(
                        onClick = onSave,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple
                    )
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.save) ,
                    contentDescription = "Save expense note" ,
                    modifier = Modifier.size(25.dp) ,
                    tint = Color.Black
                )
            }
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(0.8f))
                    .clickable {
                        context.shareText(value)
                    }
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(com.zzz.core.R.drawable.send) ,
                    contentDescription = "Share expense note" ,
                    modifier = Modifier.size(25.dp) ,
                    tint = Color.Black
                )
            }

        }


    }

}

@Preview
@Composable
private fun BookField() {
    WanderaTheme {

    }
}