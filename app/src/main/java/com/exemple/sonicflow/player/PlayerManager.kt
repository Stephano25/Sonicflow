package com.exemple.sonicflow.player

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.exemple.sonicflow.data.model.Song

class PlayerManager(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private var playlist: List<Song> = emptyList()
    private var onSongChanged: ((Int) -> Unit)? = null

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player.setAudioAttributes(audioAttributes, true)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.volume = 1f

        player.addListener(object : Player.Listener {

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                val index = player.currentMediaItemIndex
                if (index in playlist.indices) {
                    onSongChanged?.invoke(index)
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                error.printStackTrace()
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

        songs.forEach {
            player.addMediaItem(MediaItem.fromUri(it.uri))
        }

        player.prepare()
    }

    fun play(song: Song) {
        if (playlist.isEmpty()) return

        val index = playlist.indexOf(song)
        if (index == -1) return

        player.seekTo(index, 0L)
        player.playWhenReady = true
        player.volume = 1f
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause()
        else {
            player.playWhenReady = true
            player.volume = 1f
        }
    }

    fun next() {
        if (player.hasNextMediaItem()) {
            player.seekToNext()
        }
    }

    fun prev() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPrevious()
        }
    }

    fun release() {
        player.release()
    }
}
