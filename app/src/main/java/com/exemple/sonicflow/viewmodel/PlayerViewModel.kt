package com.exemple.sonicflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.repository.MusicRepository
import com.exemple.sonicflow.player.PlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MusicRepository(app.contentResolver)
    private val playerManager = PlayerManager(app)

    val songs: List<Song> = repository.getAllSongs()
    private var index = 0

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun play(song: Song) {
        index = songs.indexOf(song).takeIf { it >= 0 } ?: 0
        playerManager.play(songs[index])
        _isPlaying.value = true
    }

    fun playPause() {
        if (_isPlaying.value) {
            playerManager.pause()
        } else {
            playerManager.play(songs[index])
        }
        _isPlaying.value = !_isPlaying.value
    }

    fun next() {
        index = (index + 1) % songs.size
        playerManager.play(songs[index])
        _isPlaying.value = true
    }

    fun previous() {
        index = if (index > 0) index - 1 else songs.lastIndex
        playerManager.play(songs[index])
        _isPlaying.value = true
    }

    companion object {
        fun Factory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.AndroidViewModelFactory(app) {}
    }
}
