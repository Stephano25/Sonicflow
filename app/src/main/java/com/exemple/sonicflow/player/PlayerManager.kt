package com.exemple.sonicflow.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.exemple.sonicflow.data.model.Song

@OptIn(UnstableApi::class)
class PlayerManager(context: Context) {

    private val appContext = context.applicationContext
    val player: ExoPlayer = ExoPlayer.Builder(appContext).build()

    private var playlist: List<Song> = emptyList()
    private var onSongChanged: ((Int) -> Unit)? = null

    // Amplitude simulée pour le waveform
    var currentAmplitude: Int = 0
        private set

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
                val index = player.currentMediaItemIndex
                if (index in playlist.indices) onSongChanged?.invoke(index)
            }

            override fun onPlayerError(error: PlaybackException) {
                println("PLAYER ERROR: ${error.message}")
            }
        })
    }

    fun setOnSongChanged(listener: (Int) -> Unit) {
        onSongChanged = listener
    }

    fun setPlaylist(songs: List<Song>) {
        playlist = songs
        player.stop()
        player.clearMediaItems()
        val mediaItems = songs.map { MediaItem.fromUri(it.uri) }
        player.setMediaItems(mediaItems)
        player.prepare()
    }

    fun play(song: Song) {
        if (playlist.isEmpty()) return
        val index = playlist.indexOf(song)
        if (index == -1) return
        player.seekTo(index, 0)
        player.playWhenReady = true
        player.play()
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun next() {
        if (player.hasNextMediaItem()) player.seekToNext()
    }

    fun prev() {
        if (player.hasPreviousMediaItem()) player.seekToPrevious()
    }

    fun seekTo(position: Long) { player.seekTo(position) }
    fun getCurrentPosition(): Long = player.currentPosition
    fun getDuration(): Long = if (player.duration > 0) player.duration else 0L
    fun release() { player.release() }

    // ---------------- Amplitude simulation ----------------
    fun updateAmplitude() {
        currentAmplitude = if (player.isPlaying) (50..150).random() else 0
    }

    fun toggleShuffle() {
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }
}
