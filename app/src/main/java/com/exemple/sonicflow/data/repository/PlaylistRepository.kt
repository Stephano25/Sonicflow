package com.exemple.sonicflow.data.repository

import com.exemple.sonicflow.data.room.*

class PlaylistRepository(private val dao: PlaylistDao) {

    suspend fun createPlaylist(name: String): Long {
        return dao.insertPlaylist(Playlist(name = name))
    }

    suspend fun getPlaylists(): List<Playlist> = dao.getAllPlaylists()

    suspend fun addSongToPlaylist(playlistId: Long, song: PlaylistSong) {
        dao.insertSong(song.copy(playlistId = playlistId))
    }

    suspend fun getSongsForPlaylist(playlistId: Long): List<PlaylistSong> =
        dao.getSongsForPlaylist(playlistId)

    suspend fun deletePlaylist(playlist: Playlist) = dao.deletePlaylist(playlist)
    suspend fun deleteSong(song: PlaylistSong) = dao.deleteSong(song)
}
