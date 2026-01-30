package com.exemple.sonicflow.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "songUri"])
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val songUri: String
)
