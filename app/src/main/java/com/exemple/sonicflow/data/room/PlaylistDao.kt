package com.exemple.sonicflow.data.room

import androidx.room.*

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<Playlist>

    @Insert
    suspend fun insertSong(song: PlaylistSong)

    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun getSongsForPlaylist(playlistId: Long): List<PlaylistSong>

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Delete
    suspend fun deleteSong(song: PlaylistSong)
}
