package com.exemple.sonicflow.ui.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WaveformVisualizer(amplitudes: List<Int>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        if (amplitudes.isEmpty()) return@Canvas
        val barWidth = size.width / amplitudes.size
        amplitudes.forEachIndexed { i, h ->
            drawRect(
                color = Color.Cyan,
                topLeft = Offset(i * barWidth, size.height - h.toFloat()),
                size = Size(barWidth * 0.8f, h.toFloat())
            )
        }
    }
}
