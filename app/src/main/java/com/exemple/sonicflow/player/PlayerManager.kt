package com.exemple.sonicflow.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.exemple.sonicflow.data.model.Song

class PlayerManager(private val context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private var playlist: List<Song> = emptyList()
    private var currentIndex = 0

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    next() // ✅ passe automatiquement à la suivante
                }
            }
        })
    }

    fun setPlaylist(songs: List<Song>) {
        playlist = songs
        currentIndex = 0
        player.clearMediaItems()
        songs.forEach { song ->
            val mediaItem = MediaItem.fromUri(song.uri)
            player.addMediaItem(mediaItem)
        }
        player.prepare()
    }


    fun play(song: Song) {
        currentIndex = playlist.indexOf(song).takeIf { it >= 0 } ?: 0
        val mediaItem = MediaItem.fromUri(song.uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        saveState() // ✅ sauvegarde l’état
    }

    fun next() {
        if (playlist.isNotEmpty()) {
            currentIndex = (currentIndex + 1) % playlist.size
            play(playlist[currentIndex])
        }
    }

    fun prev() {
        if (playlist.isNotEmpty()) {
            currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
            play(playlist[currentIndex])
        }
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun release() {
        saveState()
        player.release()
    }

    fun getCurrentSong(): Song? {
        return if (playlist.isNotEmpty() && currentIndex in playlist.indices) {
            playlist[currentIndex]
        } else null
    }

    // ✅ Persistance avec SharedPreferences
    private fun saveState() {
        val prefs = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("currentIndex", currentIndex)
            .apply()
    }

    fun restoreState() {
        val prefs = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
        currentIndex = prefs.getInt("currentIndex", 0)
        if (playlist.isNotEmpty() && currentIndex in playlist.indices) {
            play(playlist[currentIndex])
        }
    }
}
