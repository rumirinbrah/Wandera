package com.zzz.core.presentation.image_picker.tabs

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.image_picker.viewmodel.GalleryImage
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@Composable
fun RecentImagesPage(
    images : List<GalleryImage>,
    loadMore : ()->Unit,
    onImageSelect : (image : Uri)->Unit,
    onDismiss: ()->Unit,
    modifier: Modifier = Modifier,
    gridCells : Int = 3,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val listState = rememberLazyGridState()

    BackHandler {
        onDismiss()
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo
        }.debounce(1000)
            .collect{layoutInfo->

            val total = layoutInfo.totalItemsCount
            val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            if(total >= 5 && lastItem >= total-2){
                loadMore()
            }
        }
    }

    Column(
        modifier.fillMaxSize()
    ) {
        VerticalSpace(10.dp)
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(gridCells),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(
                images,
                key = {
                    it.id
                }
            ){image->
                Box(
                    Modifier.animateItem()
                        .weight(1f)
                        .clickable {
                            onImageSelect(image.image)
                        }
                ){
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(image.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = "image",
                        modifier = Modifier
                            .aspectRatio(1f) ,
                        contentScale = ContentScale.Crop
                    )
                }

            }
            item {
                VerticalSpace()
            }
            item {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    TextButton(
                        onClick = {
                            loadMore()
                        }
                    ) {
                        Text("Load more...")
                    }
                }
            }
        }

    }
}