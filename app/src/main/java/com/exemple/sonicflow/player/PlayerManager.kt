package com.exemple.sonicflow.player

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.exemple.sonicflow.data.model.Song
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.random.Random

class PlayerManager(context: Context) {

    private val appContext = context.applicationContext
    val player: ExoPlayer = ExoPlayer.Builder(appContext).build()

    private var playlist: List<Song> = emptyList()
    private var currentIndex = 0
    private var onSongChanged: ((Int) -> Unit)? = null

    // Amplitude simulée pour le waveform
    var currentAmplitude: Int = 0
        private set

    private val amplitudeJob = Job()
    private val amplitudeScope = CoroutineScope(Dispatchers.Main + amplitudeJob) // Changé à Main

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player.setAudioAttributes(audioAttributes, true)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.playWhenReady = false

        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentIndex = player.currentMediaItemIndex
                onSongChanged?.invoke(currentIndex)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                // Mettre à jour l'état de lecture
            }

            override fun onPlayerError(error: PlaybackException) {
                println("PLAYER ERROR: ${error.message}")
            }
        })

        // Démarrer la simulation d'amplitude
        startAmplitudeSimulation()
    }

    private fun startAmplitudeSimulation() {
        amplitudeScope.launch {
            var phase = 0.0
            while (isActive) {
                if (player.isPlaying) {  // Maintenant sur le bon thread
                    // Simulation d'amplitude
                    phase += 0.1
                    val baseAmplitude = 50 + (sin(phase) * 30).toInt()
                    val randomVariation = Random.nextInt(-15, 15)
                    currentAmplitude = (baseAmplitude + randomVariation).coerceIn(20, 150)
                } else {
                    currentAmplitude = 0
                }
                delay(50)
            }
        }
    }

    fun setOnSongChanged(listener: (Int) -> Unit) {
        onSongChanged = listener
    }

    fun setPlaylist(songs: List<Song>) {
        playlist = songs
        currentIndex = 0
        player.stop()
        player.clearMediaItems()
        val mediaItems = songs.map { MediaItem.fromUri(it.uri) }
        player.setMediaItems(mediaItems)
        player.prepare()
    }

    fun play(song: Song) {
        val index = playlist.indexOfFirst { it.id == song.id }
        if (index == -1) return

        currentIndex = index
        player.seekTo(currentIndex, 0)
        player.play()
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun pause() {
        player.pause()
    }

    fun next() {
        if (playlist.isEmpty()) return
        player.seekToNext()
        currentIndex = player.currentMediaItemIndex
    }

    fun prev() {
        if (playlist.isEmpty()) return
        player.seekToPrevious()
        currentIndex = player.currentMediaItemIndex
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun getCurrentPosition(): Long = player.currentPosition

    fun getDuration(): Long = if (player.duration > 0) player.duration else 0L

    fun isPlaying(): Boolean = player.isPlaying

    fun getCurrentSong(): Song? {
        return if (currentIndex in playlist.indices) {
            playlist[currentIndex]
        } else null
    }

    fun release() {
        amplitudeJob.cancel()
        player.release()
    }

    fun toggleShuffle() {
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }

    fun setRepeatMode(mode: Int) {
        player.repeatMode = mode
    }

    fun updateAmplitude() {
        // Déjà géré par la coroutine
    }
}