package com.exemple.sonicflow.data.room

import android.net.Uri
import com.exemple.sonicflow.data.model.Song

// Extension pour convertir PlaylistSong en Song
fun PlaylistSong.toSong(): Song {
    return Song(
        id = this.songId,
        title = this.title,
        artist = this.artist,
        album = this.album,
        uri = Uri.parse(this.uri)
    )
}
