package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import kotlinx.coroutines.delay

@Composable
fun WaveformVisualizer(
    amplitudes: List<Int>,
    progress: Float,
    modifier: Modifier = Modifier
) {
    // ✅ Récupérer les couleurs dans un contexte Composable
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Canvas(modifier = modifier.fillMaxWidth().height(100.dp)) {
        if (amplitudes.isEmpty()) return@Canvas
        val barWidth = size.width / amplitudes.size
        amplitudes.forEachIndexed { index, amp ->
            val barHeight = (amp / 32767f) * size.height
            val x = index * barWidth
            val color = if (index / amplitudes.size.toFloat() <= progress) {
                activeColor
            } else {
                inactiveColor
            }
            drawRect(
                color = color,
                topLeft = Offset(x, size.height - barHeight),
                size = Size(barWidth * 0.8f, barHeight)
            )
        }
    }
}

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val currentSong = viewModel.currentSong

    // ✅ Progression mise à jour en temps réel
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(viewModel.isPlaying()) {
        while (viewModel.isPlaying()) {
            val duration = viewModel.getDuration()
            val position = viewModel.getCurrentPosition()
            progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
            delay(500) // mise à jour toutes les 0.5s
        }
    }

    // ⚡ Amplitudes (placeholder, à remplacer par Room ou MediaExtractor)
    val amplitudes = remember { listOf(1000, 5000, 12000, 8000, 6000, 3000, 9000) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(currentSong?.title ?: "No song playing", style = MaterialTheme.typography.headlineSmall)
        Text(currentSong?.artist ?: "", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        WaveformVisualizer(amplitudes, progress)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { viewModel.prev() }) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
            }
            IconButton(onClick = { viewModel.togglePlayPause() }) {
                Icon(
                    if (viewModel.isPlaying()) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "Play/Pause"
                )
            }
            IconButton(onClick = { viewModel.next() }) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Next")
            }
        }
    }
}
