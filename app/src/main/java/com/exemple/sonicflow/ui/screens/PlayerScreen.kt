package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {

    val song by remember { derivedStateOf { viewModel.currentSong } }
    val position by remember { derivedStateOf { viewModel.currentPosition } }
    val duration by remember { derivedStateOf { viewModel.duration } }
    val amplitude by remember { derivedStateOf { viewModel.currentAmplitude } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Waveform live
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(30) { i ->
                val barHeight = (amplitude / (i + 1).toFloat()).coerceAtLeast(5f)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(fraction = barHeight / 150f)
                        .padding(horizontal = 1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Song info
        Text(song?.title ?: "", style = MaterialTheme.typography.headlineSmall)
        Text(song?.artist ?: "", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Slider
        Slider(
            value = if (duration > 0) position.toFloat() / duration else 0f,
            onValueChange = {
                val newPosition = (it * duration).toLong()
                viewModel.seekTo(newPosition)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Controls
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { viewModel.prev() }) { Icon(Icons.Default.SkipPrevious, null) }
            IconButton(
                onClick = { viewModel.togglePlayPause() },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    if (viewModel.isPlaying()) Icons.Default.Pause else Icons.Default.PlayArrow,
                    null
                )
            }
            IconButton(onClick = { viewModel.next() }) { Icon(Icons.Default.SkipNext, null) }
        }
    }

}
