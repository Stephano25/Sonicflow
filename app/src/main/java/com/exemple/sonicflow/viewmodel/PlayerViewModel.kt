package com.exemple.sonicflow.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.exemple.sonicflow.data.db.AppDatabase
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.repository.MusicRepository
import com.exemple.sonicflow.player.PlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val musicRepository = MusicRepository(app.contentResolver)
    private val playerManager = PlayerManager(app)

    // --- Exposition des chansons ---
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    // --- État du lecteur ---
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    // --- Progression et durée ---
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _duration = MutableStateFlow(0f)
    val duration: StateFlow<Float> = _duration

    init {
        loadSongs()
        observeProgress()
    }

    private fun loadSongs() {
        viewModelScope.launch {
            val allSongs = musicRepository.getAllSongs()
            _songs.value = allSongs
            db.songDao().insertAll(allSongs) // stocker en base

            // ✅ Log pour vérifier
            Log.d("SONICFLOW", "Songs loaded: ${allSongs.size}")
        }
    }

    // --- Contrôles du lecteur ---
    fun play(song: Song) {
        playerManager.play(song)
        _currentSong.value = song
        _isPlaying.value = true
        _duration.value = playerManager.getDuration().toFloat()
    }

    fun playPause() {
        if (_isPlaying.value) {
            playerManager.pause()
            _isPlaying.value = false
        } else {
            playerManager.resume()
            _isPlaying.value = true
        }
    }

    fun next() {
        playerManager.next()
    }

    fun previous() {
        playerManager.previous()
    }

    fun seekTo(position: Long) {
        playerManager.seekTo(position)
        _progress.value = position.toFloat()
    }

    // --- Mise à jour automatique de la progression ---
    private fun observeProgress() {
        viewModelScope.launch {
            while (true) {
                if (_isPlaying.value) {
                    _progress.value = playerManager.getCurrentPosition().toFloat()
                    _duration.value = playerManager.getDuration().toFloat()
                }
                delay(500) // mise à jour toutes les 0.5 secondes
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }

    companion object {
        fun Factory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.AndroidViewModelFactory(app) {}
    }
}
