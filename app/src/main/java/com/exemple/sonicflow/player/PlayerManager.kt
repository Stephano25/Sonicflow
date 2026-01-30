package com.exemple.sonicflow.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.exemple.sonicflow.data.model.Song

class PlayerManager(context: Context) {
    private val player = ExoPlayer.Builder(context).build()

    fun play(song: Song) {
        player.setMediaItem(MediaItem.fromUri(song.songUri))
        player.prepare()
        player.play()
    }

    fun pause() = player.pause()
    fun resume() = player.play()
    fun next() = player.seekToNext()
    fun previous() = player.seekToPrevious()
    fun seekTo(position: Long) = player.seekTo(position)
    fun isPlaying(): Boolean = player.isPlaying
    fun getDuration(): Long = player.duration
    fun getCurrentPosition(): Long = player.currentPosition
    fun release() = player.release()
    fun getPlayer(): ExoPlayer = player
}
