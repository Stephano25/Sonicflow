package com.exemple.sonicflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey val songUri: String,
    val title: String,
    val artist: String
)
