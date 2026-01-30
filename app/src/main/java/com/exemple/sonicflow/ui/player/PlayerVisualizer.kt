package com.exemple.sonicflow.ui.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun PlayerVisualizer() {
    val amplitudes = List(20) { Random.nextInt(10, 100) }

    Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        val barWidth = size.width / amplitudes.size
        amplitudes.forEachIndexed { i, h ->
            drawRect(
                color = Color.Magenta,
                topLeft = Offset(i * barWidth, size.height - h.toFloat()),
                size = Size(barWidth * 0.8f, h.toFloat())
            )
        }
    }
}
