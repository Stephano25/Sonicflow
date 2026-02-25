package com.exemple.sonicflow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

@Composable
fun WaveformVisualizer(
    amplitudes: List<Float>,
    progress: Float,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val backgroundColor = MaterialTheme.colorScheme.background

    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animation pour les vagues
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(backgroundColor.copy(alpha = 0.1f))
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerY = canvasHeight / 2

        // Dessiner la ligne de base
        drawLine(
            color = secondaryColor.copy(alpha = 0.3f),
            start = Offset(0f, centerY),
            end = Offset(canvasWidth, centerY),
            strokeWidth = 1.dp.toPx()
        )

        if (amplitudes.isEmpty() || amplitudes.all { it == 0f }) {
            // Waveform animé par défaut (style table de mixage)
            val barCount = 60
            val barWidth = canvasWidth / barCount

            for (i in 0 until barCount) {
                val x = i * barWidth
                val normalizedPosition = i.toFloat() / barCount

                // Calculer la hauteur avec plusieurs ondes sinusoïdales
                val wave1 = sin(phase + i * 0.3f).toFloat() * 0.4f
                val wave2 = cos(phase * 1.5f + i * 0.2f).toFloat() * 0.3f
                val wave3 = sin(phase * 2f + i * 0.5f).toFloat() * 0.2f

                var waveHeight = if (isPlaying) {
                    (wave1 + wave2 + wave3) * 0.8f + 0.5f
                } else {
                    // Barres statiques quand la musique est en pause
                    0.2f + 0.1f * sin(i * 0.3f).toFloat()
                }

                waveHeight = waveHeight.coerceIn(0.2f, 1f)

                val barHeight = canvasHeight * 0.7f * waveHeight
                val barY = centerY - barHeight / 2

                // Gradient vertical pour chaque barre
                val gradient = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor,
                        secondaryColor,
                        primaryColor.copy(alpha = 0.7f)
                    )
                )

                // Forme de barre arrondie
                drawRoundRect(
                    brush = if (normalizedPosition <= progress) gradient else gradient,
                    topLeft = Offset(x + 2f, barY),
                    size = Size(barWidth - 4f, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                )

                // Effet de brillance
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.1f * (if (normalizedPosition <= progress) pulse else 0.5f)),
                    topLeft = Offset(x + 2f, barY),
                    size = Size(barWidth - 4f, barHeight * 0.2f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                )
            }

            // Ajouter une courbe de niveau
            val path = Path()
            for (i in 0..canvasWidth.toInt() step 2) {
                val x = i.toFloat()
                val t = x / canvasWidth
                val wave = sin(phase * 2 + t * 20).toFloat() * cos(phase + t * 10).toFloat() * 20
                val y = centerY + wave

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = primaryColor.copy(alpha = 0.2f),
                style = Stroke(width = 2.dp.toPx())
            )

        } else {
            // Waveform réel
            val barWidth = canvasWidth / amplitudes.size

            amplitudes.forEachIndexed { index, amp ->
                val x = index * barWidth
                val normalizedPosition = index.toFloat() / amplitudes.size

                val barHeight = canvasHeight * 0.8f * amp.coerceIn(0.1f, 1f)
                val barY = centerY - barHeight / 2

                val gradient = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor,
                        secondaryColor
                    )
                )

                drawRoundRect(
                    brush = if (normalizedPosition <= progress) gradient else gradient,
                    topLeft = Offset(x + 2f, barY),
                    size = Size(barWidth - 4f, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                )

                // Effet lumineux sur les barres actives
                if (normalizedPosition <= progress && isPlaying) {
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.2f * pulse),
                        topLeft = Offset(x + 2f, barY),
                        size = Size(barWidth - 4f, barHeight * 0.1f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                    )
                }
            }
        }
    }
}