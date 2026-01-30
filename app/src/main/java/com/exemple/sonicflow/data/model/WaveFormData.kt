package com.exemple.sonicflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaveformData(
    @PrimaryKey val songUri: String,
    val amplitudes: List<Int>
)
