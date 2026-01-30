package com.exemple.sonicflow.data.repository

import com.exemple.sonicflow.data.db.WaveformDao
import com.exemple.sonicflow.data.model.WaveformData

class WaveformRepository(private val dao: WaveformDao) {
    suspend fun saveWaveform(uri: String, amplitudes: List<Int>) {
        dao.insertWaveform(WaveformData(uri, amplitudes))
    }

    suspend fun getWaveform(uri: String): List<Int>? {
        return dao.getWaveform(uri)?.amplitudes
    }
}
