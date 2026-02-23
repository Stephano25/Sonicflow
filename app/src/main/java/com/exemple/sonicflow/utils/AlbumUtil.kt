package com.exemple.sonicflow.utils

import android.content.ContentUris
import android.net.Uri

object AlbumArtUtil {
    fun getAlbumArtUri(songId: Long): Uri {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            songId
        )
    }
}