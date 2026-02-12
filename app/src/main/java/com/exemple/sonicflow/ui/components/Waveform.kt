package com.exemple.sonicflow.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun Waveform(modifier: Modifier = Modifier) {

    val bars = remember {
        List(40) { Random.nextFloat() }
    }

    Canvas(modifier = modifier) {
        val width = size.width / bars.size
        bars.forEachIndexed { i, value ->
            drawRect(
                color = Color.Green,
                topLeft = androidx.compose.ui.geometry.Offset(
                    i * width,
                    size.height * (1 - value)
                ),
                size = androidx.compose.ui.geometry.Size(
                    width / 2,
                    size.height * value
                )
            )
        }
    }
}
