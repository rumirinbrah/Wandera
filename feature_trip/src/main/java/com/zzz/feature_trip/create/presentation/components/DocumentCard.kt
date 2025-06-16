package com.zzz.feature_trip.create.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.components.ImageComponentWithDefaultBackground
import com.zzz.core.util.isMimeTypeImg
import com.zzz.core.util.openImageInGallery
import com.zzz.core.util.openPDF
import com.zzz.data.trip.model.UserDocument

@Composable
internal fun DocumentCard(
    document: UserDocument,
    onDelete : (docId : Long)->Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var isImage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        println("DOC CARD Launched effect for ${document.docName}")
        isImage = context.isMimeTypeImg(document.uri)
    }

    Row (
        modifier.fillMaxWidth()
            .clip(Shapes().large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                if(isImage){
                    context.openImageInGallery(document.uri)
                }else{
                    context.openPDF(document.uri)
                }
            }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = document.docName,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            AnimatedVisibility(isImage) {
                ImageComponentWithDefaultBackground(
                    title = document.docName,
                    imageUri = document.uri,
                    modifier = Modifier.size(70.dp)
                        .clip(Shapes().medium)
                )
            }
        }
        IconButton(
            onClick = {
                onDelete(document.id)
            }
        ) {
            Icon(
                painter = painterResource(com.zzz.core.R.drawable.delete) ,
                contentDescription = document.docName,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }


}