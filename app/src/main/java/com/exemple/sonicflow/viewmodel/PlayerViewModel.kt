package com.exemple.sonicflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.repository.MusicRepository
import com.exemple.sonicflow.data.repository.PlaylistRepository
import com.exemple.sonicflow.data.room.AppDatabase
import com.exemple.sonicflow.data.room.Playlist
import com.exemple.sonicflow.data.room.PlaylistSong
import com.exemple.sonicflow.data.room.toSong
import com.exemple.sonicflow.player.PlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = MusicRepository(app)
    private val manager = PlayerManager(app)
    private val db = AppDatabase.getInstance(app)
    private val playlistRepo = PlaylistRepository(db.playlistDao())

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // Waveform simplifié (animation seulement, pas de temps réel)
    private val _waveform = MutableStateFlow<List<Float>>(
        List(50) { index -> 0.3f + 0.1f * (index % 3) }
    )
    val waveform: StateFlow<List<Float>> = _waveform.asStateFlow()

    init {
        manager.setOnSongChanged { index ->
            viewModelScope.launch {
                _songs.value.getOrNull(index)?.let { song ->
                    _currentSong.value = song
                }
            }
        }

        viewModelScope.launch {
            while (true) {
                _currentPosition.value = manager.getCurrentPosition()
                _duration.value = manager.getDuration()
                _isPlaying.value = manager.isPlaying()
                delay(200)
            }
        }
    }

    fun loadSongs() {
        viewModelScope.launch {
            try {
                val loadedSongs = withContext(Dispatchers.IO) {
                    repository.getAllSongs()
                }
                _songs.value = loadedSongs
                if (loadedSongs.isNotEmpty()) {
                    manager.setPlaylist(loadedSongs)
                    _currentSong.value = loadedSongs.first()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun play(song: Song) {
        manager.play(song)
        _currentSong.value = song
    }

    fun togglePlayPause() = manager.togglePlayPause()
    fun next() {
        manager.next()
        _currentSong.value = manager.getCurrentSong()
    }
    fun prev() {
        manager.prev()
        _currentSong.value = manager.getCurrentSong()
    }
    fun seekTo(position: Long) = manager.seekTo(position)
    fun pause() = manager.pause()
    fun toggleShuffle() = manager.toggleShuffle()
    fun setRepeatMode(mode: Int) = manager.setRepeatMode(mode)

    // Playlists
    suspend fun getPlaylists(): List<Playlist> = withContext(Dispatchers.IO) {
        try { playlistRepo.getPlaylists() } catch (e: Exception) { emptyList() }
    }

    suspend fun createPlaylist(name: String): Long = withContext(Dispatchers.IO) {
        try { playlistRepo.createPlaylist(name) } catch (e: Exception) { -1 }
    }

    suspend fun addSongToPlaylist(playlistId: Long, song: Song) {
        withContext(Dispatchers.IO) {
            try {
                val playlistSong = PlaylistSong(
                    playlistId = playlistId,
                    songId = song.id,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    uri = song.uri.toString(),
                    albumId = song.albumId
                )
                playlistRepo.addSongToPlaylist(playlistId, playlistSong)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun getSongsFromPlaylist(playlistId: Long): List<Song> = withContext(Dispatchers.IO) {
        try { playlistRepo.getSongsForPlaylist(playlistId).map { it.toSong() } }
        catch (e: Exception) { emptyList() }
    }

    override fun onCleared() {
        manager.release()
        super.onCleared()
    }
}