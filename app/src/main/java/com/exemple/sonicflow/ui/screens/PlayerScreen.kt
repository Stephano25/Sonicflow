package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exemple.sonicflow.ui.player.WaveformVisualizer
import com.exemple.sonicflow.utils.AlbumArtUtil
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {

    val song = viewModel.currentSong
    val position = viewModel.currentPosition
    val duration = viewModel.duration

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (song != null) {

            AsyncImage(
                model = AlbumArtUtil.getAlbumArtUri(song.id),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(song.title)
            Text(song.artist)

            Spacer(modifier = Modifier.height(24.dp))

            WaveformVisualizer(
                amplitudes = viewModel.waveform,
                progress = if (duration > 0) position.toFloat() / duration else 0f
            )

            Spacer(modifier = Modifier.height(24.dp))

            Slider(
                value = if (duration > 0) position.toFloat() / duration else 0f,
                onValueChange = {
                    viewModel.seekTo((it * duration).toLong())
                }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(onClick = { viewModel.prev() }) {
                    Icon(Icons.Default.SkipPrevious, null)
                }

                IconButton(onClick = { viewModel.togglePlayPause() }) {
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
}