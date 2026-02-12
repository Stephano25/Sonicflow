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
import androidx.compose.runtime.mutableStateOf

class PlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = MusicRepository(app)
    private val manager = PlayerManager(app)

    var currentSong by mutableStateOf<Song?>(null)
        private set

    var songs = mutableStateListOf<Song>()
        private set

    fun play(song: Song) {
        currentSong = song
        manager.play(song)
    }

    fun togglePlayPause() {
        if (manager.player.isPlaying) {
            manager.player.pause()
        } else {
            manager.player.play()
        }
    }

    fun loadSongs() {
        songs.clear()
        songs.addAll(repository.getAllSongs())
    }

    override fun onCleared() {
        super.onCleared()
        manager.release()
    }
}
