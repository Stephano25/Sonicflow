package com.exemple.sonicflow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.sin

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
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barCount = amplitudes.size
        val barWidth = canvasWidth / barCount

        for (i in amplitudes.indices) {
            val x = i * barWidth
            val normalizedPosition = i.toFloat() / barCount

            var amp = amplitudes[i].coerceIn(0.2f, 1f)
            if (isPlaying && normalizedPosition <= progress) {
                amp *= pulse
            }

            val barHeight = canvasHeight * 0.8f * amp
            val barY = (canvasHeight - barHeight) / 2

            val color = if (normalizedPosition <= progress) {
                primaryColor
            } else {
                secondaryColor.copy(alpha = 0.3f)
            }

            drawRect(
                color = color,
                topLeft = Offset(x + 2f, barY),
                size = Size(barWidth - 4f, barHeight)
            )
        }
    }
}