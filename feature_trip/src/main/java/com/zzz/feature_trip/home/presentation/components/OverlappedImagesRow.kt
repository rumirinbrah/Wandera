package com.zzz.feature_trip.home.presentation.components

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.ImageComponent
import com.zzz.core.theme.WanderaTheme

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
                    contentDescription = "Day image" ,
                    imageUri = uri ,
                    background = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(imageSize)
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
                    contentDescription = "$remainingImages images more" ,
                    imageUri = null ,
                    background = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(imageSize)
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

