package com.exemple.sonicflow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun WaveformVisualizer(
    amplitudes: List<Float>,
    progress: Float,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        if (amplitudes.isEmpty()) {
            // Afficher un waveform animé par défaut
            val barCount = 50
            val barWidth = size.width / barCount

            for (i in 0 until barCount) {
                val normalizedPosition = i.toFloat() / barCount
                val animatedHeight = if (isPlaying) {
                    ((sin(normalizedPosition * 2 * PI + System.currentTimeMillis() / 500.0) * 0.5) + 0.5).toFloat() * 0.5f + 0.5f
                } else {
                    0.3f
                }

                val barHeight = size.height * (0.2f + animatedHeight * 0.6f)
                val x = i * barWidth

                val color = if (normalizedPosition <= progress) {
                    primaryColor
                } else {
                    secondaryColor.copy(alpha = 0.3f)
                }

                drawRect(
                    color = color,
                    topLeft = Offset(x, size.height - barHeight),
                    size = Size(barWidth * 0.7f, barHeight),
                    alpha = if (isPlaying) pulse else 0.7f
                )
            }
        } else {
            val barWidth = size.width / amplitudes.size

            amplitudes.forEachIndexed { index, amp ->
                val barHeight = size.height * (0.2f + amp * 0.8f)
                val x = index * barWidth

                val normalizedPosition = index.toFloat() / amplitudes.size
                val color = if (normalizedPosition <= progress) {
                    primaryColor
                } else {
                    secondaryColor.copy(alpha = 0.3f)
                }

                drawRect(
                    color = color,
                    topLeft = Offset(x, size.height - barHeight),
                    size = Size(barWidth * 0.7f, barHeight),
                    alpha = if (isPlaying && normalizedPosition <= progress) pulse else 1f
                )
            }
        }
    }
}