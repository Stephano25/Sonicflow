package com.exemple.sonicflow.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exemple.sonicflow.data.model.Song

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<Song>)

    @Query("SELECT * FROM Song")
    suspend fun getAllSongs(): List<Song>

    @Query("DELETE FROM Song WHERE songUri = :songUri")
    suspend fun deleteSong(songUri: String)
}
