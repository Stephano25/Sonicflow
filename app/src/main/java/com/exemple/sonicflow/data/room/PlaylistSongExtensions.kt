package com.exemple.sonicflow.data.room

import android.net.Uri
import com.exemple.sonicflow.data.model.Song

// ✅ Extension pour convertir PlaylistSong en Song utilisable par ExoPlayer
fun PlaylistSong.toSong(): Song {
    return Song(
        id = songId,
        title = title,
        artist = artist,
        album = album,
        uri = Uri.parse(uri)
    )
}
