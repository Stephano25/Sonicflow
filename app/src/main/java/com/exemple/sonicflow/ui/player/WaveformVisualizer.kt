package com.exemple.sonicflow.ui.player

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun WaveformVisualizer(
    amplitudes: List<Int>,
    progress: Float,
    modifier: Modifier = Modifier
) {

    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {

        if (amplitudes.isEmpty()) return@Canvas

        val barWidth = size.width / amplitudes.size

        amplitudes.forEachIndexed { index, amp ->

            val normalized = (amp / 32767f).coerceIn(0f, 1f)
            val barHeight = normalized * size.height
            val x = index * barWidth

            val color =
                if (index / amplitudes.size.toFloat() <= progress)
                    activeColor
                else
                    inactiveColor

            drawRect(
                color = color,
                topLeft = Offset(x, size.height - barHeight),
                size = Size(barWidth * 0.7f, barHeight)
            )
        }
    }
}
