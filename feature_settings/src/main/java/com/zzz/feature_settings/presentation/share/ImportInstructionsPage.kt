package com.zzz.feature_settings.presentation.share

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.feature_settings.R

@Composable
fun ImportInstructionsPage(
    navUp : ()->Unit,
    modifier: Modifier = Modifier
) {
    val columnScrollState = rememberScrollState()

    Column(
        modifier.fillMaxSize()
            .verticalScroll(columnScrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButtonHeader(
            actionIcon = com.zzz.core.R.drawable.arrow_back ,
            title = "Import Trip" ,
            fontSize = 20.sp ,
            itemsSpacing = 8.dp ,
            fontWeight = FontWeight.Bold ,
            onAction = {
                navUp()
            }
        )

        VerticalSpace()
        Text(
            text = buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        fontSize = 16.sp
                    )
                )
                append("Don't know how to import the trips shared by your friends? Don't worry, I got ya!\n\n")
                append("Now, you must have received a file with an extension ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary ,
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append(".JSON")
                }
                append(" right?\n")
                append("Long click on that file and choose the ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("SHARE")
                }
                append(" option.")
            }
        )

        //IMAGE
        Image(
            painter = painterResource(R.drawable.import_3),
            contentDescription = "Use share button image",
            modifier = Modifier.clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(R.drawable.import_1),
            contentDescription = "Use share button image",
            modifier = Modifier.clip(MaterialTheme.shapes.medium)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        VerticalSpace()
        HorizontalDivider(thickness = 2.dp)
        Text(
            text = buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        fontSize = 16.sp
                    )
                )
                append("Once the bottom sheet appears, choose ")

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("Wandera")
                }
                append(" from the options, that's all you gotta do!")
            }
        )
        VerticalSpace(10.dp)

        Image(
            painter = painterResource(R.drawable.import_2),
            contentDescription = "Choose Wandera from bottom sheet image",
            modifier = Modifier.clip(MaterialTheme.shapes.medium)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        VerticalSpace()
    }
}