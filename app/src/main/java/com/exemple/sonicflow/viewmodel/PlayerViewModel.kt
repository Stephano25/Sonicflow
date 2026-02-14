package com.exemple.sonicflow.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.repository.MusicRepository
import com.exemple.sonicflow.player.PlayerManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MusicRepository(app)
    private val manager = PlayerManager(app)

    var currentSong by mutableStateOf<Song?>(null)
        private set

    var songs = mutableStateListOf<Song>()
        private set

    fun play(song: Song) {
        manager.play(song)
        currentSong = manager.getCurrentSong()
    }

    fun togglePlayPause() {
        manager.togglePlayPause()
    }

    fun isPlaying(): Boolean = manager.player.isPlaying

    fun next() {
        manager.next()
        currentSong = manager.getCurrentSong()
    }

    fun prev() {
        manager.prev()
        currentSong = manager.getCurrentSong()
    }

    fun loadSongs() {
        songs.clear()
        songs.addAll(repository.getAllSongs())
        manager.setPlaylist(songs)
        manager.restoreState() // ✅ reprend la dernière chanson
        currentSong = manager.getCurrentSong()
    }

    override fun onCleared() {
        super.onCleared()
        manager.release()
    }
    fun getDuration(): Long = manager.player.duration
    fun getCurrentPosition(): Long = manager.player.currentPosition
}
