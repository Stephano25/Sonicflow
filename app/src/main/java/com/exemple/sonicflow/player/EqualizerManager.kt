package com.exemple.sonicflow.player

import android.media.audiofx.Equalizer

class EqualizerManager(audioSessionId: Int) {

    private var equalizer: Equalizer? = null

    init {
        if (audioSessionId != 0) {
            try {
                equalizer = Equalizer(0, audioSessionId)
                equalizer?.enabled = true
            } catch (e: Exception) {
                equalizer = null
            }
        }
    }

    fun getBands(): Short = equalizer?.numberOfBands ?: 0
    fun getRange(): ShortArray? = equalizer?.bandLevelRange

    fun setBandLevel(band: Short, level: Short) {
        equalizer?.setBandLevel(band, level)
    }

    fun release() {
        equalizer?.release()
    }
}
