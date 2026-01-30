package com.exemple.sonicflow.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["playlistId", "songUri"],
    indices = [Index(value = ["songUri"])]
)
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val songUri: String
)
