package com.zzz.core.presentation.nav

import kotlinx.serialization.Serializable

@Serializable
internal sealed class Screen{
    @Serializable
    data object AllAlbumsScreen : Screen()

    @Serializable
    data class AlbumDetailsScreen (val albumName : String ): Screen()
}
