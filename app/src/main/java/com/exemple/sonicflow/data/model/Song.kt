package com.exemple.sonicflow.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey val uri: Uri,   // ✅ maintenant Room sait gérer via Converters
    val title: String,
    val artist: String
)
