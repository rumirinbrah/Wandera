package com.zzz.feature_trip.overview.presentation.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.ImageComponent
import com.zzz.core.util.FileType
import com.zzz.core.util.getMimeType
import com.zzz.core.util.openImageInGallery
import com.zzz.core.util.openPDF
import com.zzz.data.trip.model.UserDocument
import com.zzz.feature_trip.R

/**
 * Represents the document uploaded by user. Doesn't have any delete/edit action
 */
@Composable
internal fun OverviewDocumentCard(
    document: UserDocument ,
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize : TextUnit = 15.sp,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var fileType by remember { mutableStateOf(FileType.UNKNOWN) }


    LaunchedEffect(Unit) {
        fileType = context.getMimeType(document.uri)
    }

    Row (
        modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable {
                context.openAppForTheFile(document.uri,fileType)
            }
            .padding(vertical = 8.dp, horizontal = 16.dp) ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = document.docName,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = fontSize,
            fontWeight = fontWeight,
            modifier = Modifier.padding(end = 8.dp)
                .weight(1f)
        )
        when(fileType){
            FileType.PDF -> {
                Image(
                    painter = painterResource(R.drawable.pdf_icon_coloured) ,
                    contentDescription = "pdf document",
                    modifier = Modifier.size(45.dp)
                )
            }
            FileType.IMAGE -> {
                ImageComponent(
                    contentDescription = document.docName,
                    imageUri = document.uri,
                    modifier = Modifier.size(70.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.bug_icon) ,
                        contentDescription = "pdf document",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        "corrupt!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                }
            }
        }
        /*
        AnimatedVisibility(isImage) {
            ImageComponentWithDefaultBackground(
                title = document.docName,
                imageUri = document.uri,
                modifier = Modifier.size(70.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

         */


    }

}

/**
 * Opens appropriate app for viewing the URI file
 */
private fun Context.openAppForTheFile(uri: Uri,fileType : FileType){
    when(fileType){
        FileType.PDF -> {
            openPDF(uri)
        }
        FileType.IMAGE ->{
            openImageInGallery(uri)
        }
        else->Unit
    }
}