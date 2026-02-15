package com.exemple.sonicflow.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.repository.MusicRepository
import com.exemple.sonicflow.player.PlayerManager
import com.exemple.sonicflow.data.repository.PlaylistRepository
import com.exemple.sonicflow.data.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = MusicRepository(app)
    private val manager = PlayerManager(app)
    private val db = AppDatabase.getInstance(app)
    private val playlistRepo = PlaylistRepository(db.playlistDao())

    var songs = mutableStateListOf<Song>()
        private set

    var currentSong by mutableStateOf<Song?>(null)
        private set

    init {
        manager.setOnSongChanged { index ->
            if (index in songs.indices) {
                currentSong = songs[index]
            }
        }
    }

    fun loadSongs() {
        songs.clear()
        songs.addAll(repository.getAllSongs())

        if (songs.isNotEmpty()) {
            manager.setPlaylist(songs)
        }
    }

    fun play(song: Song) {
        manager.play(song)
        currentSong = song
    }

    fun togglePlayPause() = manager.togglePlayPause()

    fun next() = manager.next()

    fun prev() = manager.prev()

    fun isPlaying(): Boolean = manager.player.isPlaying

    fun getDuration(): Long =
        manager.player.duration.takeIf { it > 0 } ?: 0L

    fun getCurrentPosition(): Long =
        manager.player.currentPosition

    override fun onCleared() {
        manager.release()
        super.onCleared()
    }

    // ---------- PLAYLIST ROOM ----------

    suspend fun createPlaylist(name: String) =
        withContext(Dispatchers.IO) {
            playlistRepo.createPlaylist(name)
        }

    suspend fun getPlaylists(): List<Playlist> =
        withContext(Dispatchers.IO) {
            playlistRepo.getPlaylists()
        }

    suspend fun addSongToPlaylist(playlistId: Long, song: Song) =
        withContext(Dispatchers.IO) {
            playlistRepo.addSongToPlaylist(
                playlistId,
                PlaylistSong(
                    playlistId = playlistId,
                    songId = song.id,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    uri = song.uri.toString()
                )
            )
        }

    suspend fun getSongsForPlaylist(id: Long) =
        withContext(Dispatchers.IO) {
            playlistRepo.getSongsForPlaylist(id)
        }

    suspend fun deleteSongFromPlaylist(song: PlaylistSong) =
        withContext(Dispatchers.IO) {
            playlistRepo.deleteSong(song)
        }
}
