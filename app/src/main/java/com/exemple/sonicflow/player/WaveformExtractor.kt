package com.exemple.sonicflow.player

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import java.nio.ByteBuffer

object WaveformExtractor {
    fun extractAmplitudes(context: Context, uri: Uri): List<Int> {
        val amplitudes = mutableListOf<Int>()
        val extractor = MediaExtractor()
        extractor.setDataSource(context, uri, null)

        // On suppose que la piste audio est la première
        extractor.selectTrack(0)
        val format: MediaFormat = extractor.getTrackFormat(0)
        val bufferSize = 1024
        val buffer = ByteBuffer.allocate(bufferSize)

        while (true) {
            val sampleSize = extractor.readSampleData(buffer, 0)
            if (sampleSize < 0) break

            // Simplification : on calcule une "amplitude" moyenne par chunk
            var sum = 0
            for (i in 0 until sampleSize) {
                sum += kotlin.math.abs(buffer.get(i).toInt())
            }
            amplitudes.add(sum / sampleSize)

            extractor.advance()
        }

        extractor.release()
        return amplitudes
    }
}
