package com.exemple.sonicflow.data.model

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val uri: Uri,
    val duration: Long = 0,
    val albumId: Long = 0  // Ajout de albumId
)