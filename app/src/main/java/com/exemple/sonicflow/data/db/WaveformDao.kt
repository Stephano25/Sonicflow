package com.exemple.sonicflow.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exemple.sonicflow.data.model.WaveformData

@Dao
interface WaveformDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaveform(data: WaveformData)

    @Query("SELECT * FROM WaveformData WHERE songUri = :uri")
    suspend fun getWaveform(uri: String): WaveformData?
}
