package com.exemple.sonicflow.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.random.Random
import androidx.compose.material3.MaterialTheme

@Composable
fun Waveform(modifier: Modifier = Modifier) {
    val bars = remember { List(40) { Random.nextFloat() } }

    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Canvas(modifier = modifier) {
        val width = size.width / bars.size
        bars.forEachIndexed { i, value ->
            drawRect(
                color = if (i % 2 == 0) activeColor else inactiveColor,
                topLeft = Offset(i * width, size.height * (1 - value)),
                size = Size(width / 2, size.height * value)
            )
        }
    }
}
