package com.exemple.sonicflow.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.repository.MusicRepository
import com.exemple.sonicflow.data.repository.PlaylistRepository
import com.exemple.sonicflow.data.room.AppDatabase
import com.exemple.sonicflow.data.room.PlaylistSong
import com.exemple.sonicflow.data.room.toSong
import com.exemple.sonicflow.player.PlayerManager
import com.exemple.sonicflow.player.WaveformExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    var waveform by mutableStateOf<List<Int>>(emptyList())
        private set

    var currentPosition by mutableStateOf(0L)
        private set

    var duration by mutableStateOf(0L)
        private set

    var currentAmplitude by mutableStateOf(0)
        private set

    init {
        manager.setOnSongChanged { index ->
            if (index in songs.indices) {
                currentSong = songs[index]
                generateWaveform(getApplication(), songs[index].uri)
            }
        }

        viewModelScope.launch {
            while (true) {
                currentPosition = manager.getCurrentPosition()
                duration = manager.getDuration()
                manager.updateAmplitude()
                currentAmplitude = manager.currentAmplitude
                delay(50)
            }
        }
    }

    // ---------------- MUSIC ----------------
    fun loadSongs() {
        songs.clear()
        songs.addAll(repository.getAllSongs())
        if (songs.isNotEmpty()) {
            manager.setPlaylist(songs)
            currentSong = songs.first()
            generateWaveform(getApplication(), songs.first().uri)
        }
    }

    fun play(song: Song) {
        manager.play(song)
        currentSong = song
        generateWaveform(getApplication(), song.uri)
    }

    fun togglePlayPause() = manager.togglePlayPause()
    fun next() = manager.next()
    fun prev() = manager.prev()
    fun seekTo(pos: Long) = manager.seekTo(pos)
    fun isPlaying(): Boolean = manager.player.playbackState == Player.STATE_READY && manager.player.playWhenReady

    // ---------------- PLAYLIST ----------------
    suspend fun getPlaylists() = playlistRepo.getPlaylists()
    suspend fun createPlaylist(name: String) = playlistRepo.createPlaylist(name)
    suspend fun addSongToPlaylist(playlistId: Long, song: Song) {
        val playlistSong = PlaylistSong(
            playlistId = playlistId,
            songId = song.id,
            title = song.title,
            artist = song.artist,
            album = song.album,
            uri = song.uri.toString()
        )
        playlistRepo.addSongToPlaylist(playlistId, playlistSong)
    }
    suspend fun getSongsFromPlaylist(playlistId: Long): List<Song> =
        playlistRepo.getSongsForPlaylist(playlistId).map { it.toSong() }

    // ---------------- WAVEFORM ----------------
    private fun generateWaveform(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                WaveformExtractor.extractWaveform(context, uri)
            } catch (e: Exception) {
                emptyList()
            }
            withContext(Dispatchers.Main) {
                waveform = result
            }
        }
    }

    override fun onCleared() {
        manager.release()
        super.onCleared()
    }
}
