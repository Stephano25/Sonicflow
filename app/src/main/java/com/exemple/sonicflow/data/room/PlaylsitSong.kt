package com.exemple.sonicflow.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_songs")
data class PlaylistSong(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playlistId: Long,
    val songId: Long,
    val title: String,
    val artist: String,
    val album: String,
    val uri: String // ✅ stocké en String pour Room
)
