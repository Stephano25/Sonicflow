package com.exemple.sonicflow.player

import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.content.Context
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

object WaveformGenerator {

    suspend fun generateWaveform(context: Context, uri: Uri, samples: Int = 150): List<Float> {
        val extractor = MediaExtractor()

        return try {
            extractor.setDataSource(context, uri, null)

            var audioTrackIndex = -1
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                if (format.getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true) {
                    audioTrackIndex = i
                    break
                }
            }

            if (audioTrackIndex == -1) return emptyList()

            extractor.selectTrack(audioTrackIndex)
            val format = extractor.getTrackFormat(audioTrackIndex)
            val sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            val channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

            val buffer = ByteBuffer.allocate(8192)
            val amplitudes = mutableListOf<Float>()
            val targetSamples = samples
            val samplesPerSegment = maxOf(1, sampleRate * channelCount / 50) // ~20ms par segment

            var segmentBuffer = mutableListOf<Short>()
            var segmentCount = 0

            while (segmentCount < targetSamples) {
                val sampleSize = extractor.readSampleData(buffer, 0)
                if (sampleSize < 0) break

                buffer.rewind()
                val shortBuffer = buffer.asShortBuffer()
                val shorts = ShortArray(sampleSize / 2)
                shortBuffer.get(shorts)

                segmentBuffer.addAll(shorts.toList())

                if (segmentBuffer.size >= samplesPerSegment) {
                    val maxAmplitude = segmentBuffer.map { abs(it.toInt()) }.maxOrNull() ?: 0
                    val normalized = if (maxAmplitude > 0) {
                        (log10(maxAmplitude.toDouble() + 1) / log10(32768.0)).toFloat()
                    } else {
                        0f
                    }
                    amplitudes.add(normalized.coerceIn(0f, 1f))
                    segmentBuffer.clear()
                    segmentCount++
                }

                extractor.advance()
            }

            // Normaliser les amplitudes
            if (amplitudes.isNotEmpty()) {
                val maxAmp = amplitudes.maxOrNull() ?: 1f
                amplitudes.map { it / maxAmp }
            } else {
                emptyList()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            extractor.release()
        }
    }
}