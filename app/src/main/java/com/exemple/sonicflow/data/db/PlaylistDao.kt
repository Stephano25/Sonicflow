package com.exemple.sonicflow.data.db

import androidx.room.*
import com.exemple.sonicflow.data.model.Playlist
import com.exemple.sonicflow.data.model.PlaylistTrackCrossRef
import com.exemple.sonicflow.data.model.PlaylistWithSongs

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: PlaylistTrackCrossRef)

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :id")
    suspend fun getPlaylistWithSongs(id: Long): PlaylistWithSongs

    @Query("DELETE FROM Playlist WHERE playlistId = :id")
    suspend fun deletePlaylist(id: Long)

    @Query("DELETE FROM PlaylistTrackCrossRef WHERE playlistId = :playlistId AND songUri = :songUri")
    suspend fun removeSongFromPlaylist(playlistId: Long, songUri: String)
}
