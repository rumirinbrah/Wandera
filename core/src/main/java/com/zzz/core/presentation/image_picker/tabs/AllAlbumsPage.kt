package com.zzz.core.presentation.image_picker.tabs

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zzz.core.R
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.core.presentation.image_picker.viewmodel.GalleryAlbum
import com.zzz.core.presentation.image_picker.viewmodel.GalleryImage
import com.zzz.core.presentation.image_picker.viewmodel.ImagePickerActions
import com.zzz.core.presentation.image_picker.viewmodel.ImagePickerState
import com.zzz.core.presentation.nav.Screen

/**
 * Composable rep a list of albums and over pics. Has dedicated navigation internally
 * @param onDismiss User navigates up
 */
@Composable
internal fun AlbumsRoot(
    state: ImagePickerState ,
    onAction: (ImagePickerActions) -> Unit ,
    onDismiss: () -> Unit ,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    BackHandler {
        onDismiss()
    }

    NavHost(
        modifier = modifier ,
        navController = navController ,
        startDestination = Screen.AllAlbumsScreen ,
        enterTransition = {
            slideInHorizontally()
        } ,
        popEnterTransition = {
            slideInHorizontally()
        } ,
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = {
                    it
                }
            )
        } ,
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = {
                    it
                }
            )
        }
    ) {
        composable<Screen.AllAlbumsScreen> {
            AllAlbumsPage(
                state.albums ,
                onNavToAlbum = { albumName ->
                    navController.navigate(Screen.AlbumDetailsScreen(albumName))
                } ,
            )
        }
        composable<Screen.AlbumDetailsScreen> {
            val route = it.toRoute<Screen.AlbumDetailsScreen>()
            LaunchedEffect(Unit) {
                onAction(ImagePickerActions.LoadAlbumImages(route.albumName))
            }

            AlbumImagesPage(
                images = state.albumImages ,
                albumName = state.selectedAlbum ,
                loading = state.loadingAlbumImages,
                onImageSelect = { uri ->
                    onAction(ImagePickerActions.SelectImage(uri))
                } ,
                navigateUp = {
                    onAction(ImagePickerActions.ClearAlbumImages)
                    navController.navigateUp()
                }
            )

        }
    }

}

//---- ALBUMS ----
/**
 * Displays all the albums in device
 */
@Composable
private fun AllAlbumsPage(
    albums: List<GalleryAlbum> ,
    onNavToAlbum: (albumName: String) -> Unit ,
    modifier: Modifier = Modifier ,
    gridCells: Int = 2 ,
) {


    Column(
        modifier.fillMaxSize()
    ) {
        VerticalSpace()

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize() ,
            columns = GridCells.Fixed(gridCells) ,
            verticalArrangement = Arrangement.spacedBy(2.dp) ,
            horizontalArrangement = Arrangement.spacedBy(2.dp) ,
        ) {
            items(
                albums
            ) { album ->
                AlbumItem(
                    album ,
                    onClick = onNavToAlbum
                )
            }
        }
    }
}

//---- IMAGES ----
/**
 * Displays all the images in a particular album
 */
@Composable
private fun AlbumImagesPage(
    images: List<GalleryImage> ,
    albumName: String? = null ,
    loading : Boolean = false,
    onImageSelect: (imageUri: Uri) -> Unit ,
    navigateUp: () -> Unit ,
    modifier: Modifier = Modifier ,
    gridCells: Int = 3 ,
) {
    val context = LocalContext.current

    BackHandler {
        navigateUp()
    }

    Column(
        modifier.fillMaxSize()
    ) {
        VerticalSpace(10.dp)
        ActionButtonHeader(
            actionIcon = R.drawable.arrow_back ,
            onAction = {
                navigateUp()
            } ,
            actionDescription = "Go back" ,
            title = albumName ?: "Unknown"
        )
        VerticalSpace()
        when{
            loading->{
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize() ,
            columns = GridCells.Fixed(gridCells) ,
            verticalArrangement = Arrangement.spacedBy(2.dp) ,
            horizontalArrangement = Arrangement.spacedBy(2.dp) ,
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
                        contentDescription = "image" ,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                onImageSelect(image.image)
                            },
                        contentScale = ContentScale.Crop
                    )
                }

            }
            item {
                VerticalSpace()
            }
        }

    }
}

/**
 * Single album item in the grid
 */
@Composable
private fun AlbumItem(
    album: GalleryAlbum ,
    onClick: (albumName: String) -> Unit ,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier
            .padding(8.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(album.coverImage.image)
                .crossfade(true)
                .build() ,
            contentDescription = album.albumName ,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(1f)
                .clickable {
                    onClick(album.albumName)
                },
            contentScale = ContentScale.Crop
        )
        Text(
            album.albumName ,
            style = TextStyle(
                fontSize = 15.sp ,
                fontWeight = FontWeight.Bold
            ) ,
            maxLines = 1 ,
            overflow = TextOverflow.Ellipsis
        )
    }
}