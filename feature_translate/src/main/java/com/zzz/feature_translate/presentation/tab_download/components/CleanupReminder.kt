package com.zzz.feature_translate.presentation.tab_download.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.dialogs.DialogTemplate
import com.zzz.feature_translate.R

/**
 * @author zyzz
 * Used for showing a prompt to user reminding them to get rid of models when not needed
 */
@Composable
internal fun CleanupReminder(
    onDismiss : ()->Unit,
    modifier: Modifier = Modifier
) {
    DialogTemplate(
        onDismiss = onDismiss,
        modifier = modifier
    ){
        Text(
            text = buildAnnotatedString {
                append("The models you download are stored on your own device, and they may take up to ")
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("30-40 MB of space")
                }
                append(".Make sure to delete them when no longer needed")
            },
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
        Image(
            painter = painterResource(R.drawable.trash_image),
            contentDescription = "Trash can",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}