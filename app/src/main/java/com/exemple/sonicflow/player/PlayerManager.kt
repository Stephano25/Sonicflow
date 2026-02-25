package com.exemple.sonicflow.player

import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.audio.AudioCapabilities
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.exemple.sonicflow.data.model.Song

@OptIn(UnstableApi::class)
class PlayerManager(context: Context) {

    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Configuration audio avec gestion du focus
    private val audioSink = DefaultAudioSink.Builder()
        .setAudioCapabilities(AudioCapabilities.getCapabilities(appContext))
        .setEnableFloatOutput(true)
        .setEnableAudioTrackPlaybackParams(true)
        .build()

    private val trackSelector = DefaultTrackSelector(appContext).apply {
        setParameters(
            buildUponParameters()
                .setForceHighestSupportedBitrate(true)
        )
    }

    val player: ExoPlayer = ExoPlayer.Builder(appContext)
        .setTrackSelector(trackSelector)
        .build()

    private var playlist: List<Song> = emptyList()
    private var currentIndex = 0
    private var onSongChanged: ((Int) -> Unit)? = null

    // Listener pour gérer les changements de focus audio
    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Perte permanente - on met en pause
                if (player.isPlaying) {
                    player.pause()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Perte temporaire - on met en pause
                if (player.isPlaying) {
                    player.pause()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // On baisse le volume
                player.volume = 0.3f
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                // On regagne le focus - on remet le volume normal
                player.volume = 1.0f
                // On NE reprend PAS automatiquement la lecture
            }
        }
    }

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player.setAudioAttributes(audioAttributes, true)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.playWhenReady = false

        // IMPORTANT: Gérer correctement le bruit
        player.setHandleAudioBecomingNoisy(true)  // Met en pause quand on débranche le casque

        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentIndex = player.currentMediaItemIndex
                onSongChanged?.invoke(currentIndex)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        next()
                    }
                    Player.STATE_READY -> {
                        // Le player est prêt
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                println("PLAYER ERROR: ${error.message}")
                player.prepare()
            }
        })
    }

    fun setOnSongChanged(listener: (Int) -> Unit) {
        onSongChanged = listener
    }

    fun setPlaylist(songs: List<Song>) {
        playlist = songs
        currentIndex = 0
        if (songs.isNotEmpty()) {
            try {
                player.stop()
                player.clearMediaItems()
                val mediaItems = songs.map { MediaItem.fromUri(it.uri) }
                player.setMediaItems(mediaItems)
                player.prepare()
            } catch (e: Exception) {
                println("Error setting playlist: ${e.message}")
            }
        }
    }

    fun play(song: Song) {
        val index = playlist.indexOfFirst { it.id == song.id }
        if (index == -1) return

        currentIndex = index
        try {
            // Demander le focus audio avant de jouer
            val result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                player.seekTo(currentIndex, 0)
                player.play()
            }
        } catch (e: Exception) {
            println("Error playing song: ${e.message}")
        }
    }

    fun togglePlayPause() {
        try {
            if (player.isPlaying) {
                player.pause()
                audioManager.abandonAudioFocus(audioFocusChangeListener)
            } else {
                // Demander le focus audio avant de reprendre
                val result = audioManager.requestAudioFocus(
                    audioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    player.play()
                }
            }
        } catch (e: Exception) {
            println("Error toggling play/pause: ${e.message}")
        }
    }

    fun pause() {
        try {
            player.pause()
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        } catch (e: Exception) { }
    }

    fun next() {
        if (playlist.isEmpty()) return
        currentIndex = if (currentIndex + 1 < playlist.size) currentIndex + 1 else 0
        try {
            player.seekTo(currentIndex, 0)
            player.play()
        } catch (e: Exception) {
            println("Error playing next: ${e.message}")
        }
    }

    fun prev() {
        if (playlist.isEmpty()) return
        currentIndex = if (currentIndex - 1 >= 0) currentIndex - 1 else playlist.size - 1
        try {
            player.seekTo(currentIndex, 0)
            player.play()
        } catch (e: Exception) {
            println("Error playing previous: ${e.message}")
        }
    }

    fun seekTo(position: Long) = try { player.seekTo(position) } catch (e: Exception) { }

    fun getCurrentPosition(): Long = try { player.currentPosition } catch (e: Exception) { 0L }

    fun getDuration(): Long = try { if (player.duration > 0) player.duration else 0L } catch (e: Exception) { 0L }

    fun isPlaying(): Boolean = try { player.isPlaying } catch (e: Exception) { false }

    fun getCurrentSong(): Song? = try { if (currentIndex in playlist.indices) playlist[currentIndex] else null } catch (e: Exception) { null }

    fun release() {
        try {
            audioManager.abandonAudioFocus(audioFocusChangeListener)
            player.release()
        } catch (e: Exception) { }
    }

    fun toggleShuffle() = try { player.shuffleModeEnabled = !player.shuffleModeEnabled } catch (e: Exception) { }

    fun setRepeatMode(mode: Int) = try { player.repeatMode = mode } catch (e: Exception) { }

    fun getAudioSessionId(): Int = try { player.audioSessionId } catch (e: Exception) { 0 }
}