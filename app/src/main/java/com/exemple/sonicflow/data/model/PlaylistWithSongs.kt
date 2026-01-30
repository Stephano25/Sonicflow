package com.exemple.sonicflow.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songUri",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    )
    val songs: List<Song>
)
