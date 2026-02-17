package com.exemple.sonicflow.data.repository

import android.content.Context
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.scanner.MusicScanner

class MusicRepository(private val context: Context) {

    fun getAllSongs(): List<Song> {
        return MusicScanner.scan(context)
    }
}
