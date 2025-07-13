package com.zzz.core.presentation.image_picker.tabs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.image_picker.viewmodel.GalleryImage
import com.zzz.core.presentation.image_picker.viewmodel.ImagePickerActions
import kotlinx.coroutines.flow.debounce

/**
 * LazyCol for recent images with pagination
 * @param images Images
 * @param onDismiss Called when user discards selection
 * @param gridCells Number of images to be displayed in a row in the grid
 */
@Composable
fun RecentImagesPage(
    images: List<GalleryImage> ,
    onAction: (ImagePickerActions) -> Unit ,
    onDismiss: () -> Unit ,
    modifier: Modifier = Modifier ,
    loading: Boolean = false ,
    gridCells: Int = 3 ,
    verticalGridPadding: Dp = 2.dp ,
    horizontalGridPadding: Dp = 2.dp ,
) {
    val context = LocalContext.current

    val listState = rememberLazyGridState()

    BackHandler {
        onDismiss()
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo
        }.debounce(1000)
            .collect { layoutInfo ->

                val total = layoutInfo.totalItemsCount
                val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                //load more if we're near the END
                if (total >= 5 && lastItem >= total - 2) {
                    onAction(ImagePickerActions.LoadRecentsNextPage)
                }
            }
    }

    Column(
        modifier.fillMaxSize()
    ) {
        VerticalSpace(10.dp)

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize() ,
            columns = GridCells.Fixed(gridCells) ,
            state = listState ,
            verticalArrangement = Arrangement.spacedBy(verticalGridPadding) ,
            horizontalArrangement = Arrangement.spacedBy(horizontalGridPadding) ,
        ) {
            items(
                images ,
                key = {
                    it.id
                }
            ) { image ->
                Box(
                    Modifier
                        .animateItem()
                        .weight(1f)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(image.image)
                            .crossfade(true)
                            .build() ,
                        contentDescription = "Image" ,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                onAction(ImagePickerActions.SelectImage(image.image))
                            } ,
                        contentScale = ContentScale.Crop
                    )
                }

            }
            //some space at the bottom
            item {
                VerticalSpace()
            }
        }
        VerticalSpace(10.dp)
        Text("Can't find all you images here? Maybe you haven't given us full access to your files yet. Please go to app settings and change the 'Photos And Videos' permission from 'Allow selected' to 'Allow all'")
        Text(
            text = buildAnnotatedString {
                pushStyle(
                    style = SpanStyle(
                        fontSize = 13.sp ,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                )
                append("Can't find all you images here? Maybe you haven't given us full access to your files yet.")
                append("Please go to app settings and change the 'Photos And Videos' permission from ")
                withStyle(
                    style = SpanStyle(
                        fontSize = 13.sp ,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("'Allow Selected' to 'Allow All'")
                }
                pop()
            }
        )
        VerticalSpace(10.dp)
        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }


    }
}