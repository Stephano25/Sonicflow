package com.exemple.sonicflow.player

import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.content.Context
import java.nio.ByteBuffer
import kotlin.math.abs

object WaveformExtractor {

    fun extractWaveform(context: Context, uri: Uri): List<Int> {

        val extractor = MediaExtractor()

        return try {

            extractor.setDataSource(context, uri, null)

            var audioTrackIndex = -1

            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                if (mime?.startsWith("audio/") == true) {
                    audioTrackIndex = i
                    break
                }
            }

            if (audioTrackIndex == -1) return emptyList()

            extractor.selectTrack(audioTrackIndex)

            val buffer = ByteBuffer.allocate(2048)
            val amplitudes = mutableListOf<Int>()

            var count = 0

            while (count < 150) {

                val sampleSize = extractor.readSampleData(buffer, 0)
                if (sampleSize < 0) break

                var max = 0

                for (i in 0 until sampleSize step 2) {
                    val value = kotlin.math.abs(buffer.getShort(i).toInt())
                    if (value > max) max = value
                }

                amplitudes.add(max)
                extractor.advance()
                count++
            }

            amplitudes

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            extractor.release()
        }
    }
}

