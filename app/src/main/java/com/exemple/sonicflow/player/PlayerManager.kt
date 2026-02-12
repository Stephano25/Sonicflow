package com.exemple.sonicflow.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.exemple.sonicflow.data.model.Song

class PlayerManager(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    fun play(song: Song) {
        val mediaItem = MediaItem.fromUri(song.uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun release() {
        player.release()
    }
}
