package com.exemple.sonicflow.data.repository

import com.exemple.sonicflow.data.db.PlaylistDao
import com.exemple.sonicflow.data.db.SongDao
import com.exemple.sonicflow.data.model.Playlist
import com.exemple.sonicflow.data.model.PlaylistTrackCrossRef
import com.exemple.sonicflow.data.model.PlaylistWithSongs
import com.exemple.sonicflow.data.model.Song

class PlaylistRepository(
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao
) {
    // --- Gestion des chansons ---
    suspend fun insertSong(song: Song) = songDao.insert(song)
    suspend fun insertSongs(songs: List<Song>) = songDao.insertAll(songs)
    suspend fun getAllSongs(): List<Song> = songDao.getAllSongs()
    suspend fun deleteSong(uri: String) = songDao.deleteSong(uri)

    // --- Gestion des playlists ---
    suspend fun createPlaylist(name: String): Long {
        val playlist = Playlist(name = name)
        return playlistDao.insertPlaylist(playlist)
    }

    suspend fun addSongToPlaylist(playlistId: Long, song: Song) {
        val crossRef = PlaylistTrackCrossRef(playlistId, song.uri.toString())
        playlistDao.insertCrossRef(crossRef)
    }

    suspend fun getPlaylistWithSongs(id: Long): PlaylistWithSongs =
        playlistDao.getPlaylistWithSongs(id)

    suspend fun removeSongFromPlaylist(playlistId: Long, song: Song) {
        playlistDao.removeSongFromPlaylist(playlistId, song.uri.toString())
    }

    suspend fun deletePlaylist(id: Long) = playlistDao.deletePlaylist(id)
}
