package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import com.exemple.sonicflow.ui.player.WaveformVisualizer
import kotlinx.coroutines.delay
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {

    val song = viewModel.currentSong
    val waveform = viewModel.waveform

    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(song, viewModel.isPlaying()) {

        while (viewModel.isPlaying()) {

            val duration = viewModel.getDuration()
            val position = viewModel.getCurrentPosition()

            progress =
                if (duration > 0)
                    position.toFloat() / duration.toFloat()
                else 0f

            delay(300)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Card(
                modifier = Modifier.size(300.dp),
                shape = RoundedCornerShape(20.dp)
            ) {}

            Spacer(modifier = Modifier.height(24.dp))

            Text(song?.title ?: "")
            Text(song?.artist ?: "")
        }

        WaveformVisualizer(
            amplitudes = waveform,
            progress = progress
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(onClick = { viewModel.prev() }) {
                Icon(Icons.Default.SkipPrevious, null)
            }

            IconButton(
                onClick = { viewModel.togglePlayPause() },
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    if (viewModel.isPlaying())
                        Icons.Default.Pause
                    else
                        Icons.Default.PlayArrow,
                    null
                )
            }

            IconButton(onClick = { viewModel.next() }) {
                Icon(Icons.Default.SkipNext, null)
            }
        }
    }
}
