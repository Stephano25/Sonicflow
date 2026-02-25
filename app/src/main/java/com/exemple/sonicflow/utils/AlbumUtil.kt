package com.exemple.sonicflow.utils

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

object AlbumArtUtil {
    fun getAlbumArtUri(albumId: Long): Uri {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }

    fun getDefaultAlbumArt(): Uri {
        return Uri.parse("android.resource://com.exemple.sonicflow/drawable/default_album_art")
    }
}