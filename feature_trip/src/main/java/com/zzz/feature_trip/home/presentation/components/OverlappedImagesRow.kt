package com.zzz.feature_trip.home.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.ImageComponent
import com.zzz.core.theme.WanderaTheme
import com.zzz.feature_trip.R

@Composable
internal fun OverlappedImagesRow(
    images: List<Uri?> = emptyList() ,
    imageSize: Dp = 40.dp ,
    maxImagesVisible: Int = 5 ,
    imageBorderColor : Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    val remainingImages = remember(images) {
        derivedStateOf {
            images.size - 5
        }
    }

    Box(
        modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
    ) {
        images.take(maxImagesVisible).onEachIndexed { index , uri ->
            Box(
                Modifier
                    .offset(
                        x = imageSize / 2 * index ,
                        y = 0.dp
                    )
            ) {
                ImageComponent(
                    title = "Day image" ,
                    imageUri = uri ,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(imageSize)
                        .background(MaterialTheme.colorScheme.secondary)
                        .border(1.dp , imageBorderColor , CircleShape)
                )
            }
        }
        if(remainingImages.value>0){
            Box(
                Modifier
                    .offset(
                        x = imageSize / 2 * 5 ,
                        y = 0.dp
                    )
            ) {
                ImageComponent(
                    title = "Day image" ,
                    imageUri = null ,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(imageSize)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp , imageBorderColor , CircleShape)
                )
                Text(
                    "+${remainingImages.value}" ,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 13.sp
                )
            }
        }

    }
}



@Preview
@Composable
private fun OverlappedImagePrev() {
    WanderaTheme {
        OverlappedImagesRow(
            images = listOf(null , null , null , null)
        )
    }
}

