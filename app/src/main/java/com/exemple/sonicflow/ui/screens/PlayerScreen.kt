package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.ui.components.SleepTimerDialog
import com.exemple.sonicflow.ui.components.VinylRecord
import com.exemple.sonicflow.ui.components.WaveformVisualizer
import com.exemple.sonicflow.utils.AlbumArtUtil
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val song by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val position by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val waveform by viewModel.waveform.collectAsState()

    var showSleepTimer by remember { mutableStateOf(false) }
    var isShuffle by remember { mutableStateOf(false) }
    var repeatMode by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (song != null) {
            // Disque vinyle rotatif
            VinylRecord(
                imageUrl = AlbumArtUtil.getAlbumArtUri(song!!.albumId),
                isPlaying = isPlaying,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .weight(1f)
            )

            // Informations de la chanson
            Text(
                text = song!!.title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Text(
                text = song!!.artist,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Text(
                text = song!!.album,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Waveform
            WaveformVisualizer(
                amplitudes = waveform,
                progress = if (duration > 0) position.toFloat() / duration else 0f,
                isPlaying = isPlaying
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Barre de progression
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDuration(position),
                    style = MaterialTheme.typography.bodySmall
                )

                Slider(
                    value = if (duration > 0) position.toFloat() / duration else 0f,
                    onValueChange = { viewModel.seekTo((it * duration).toLong()) },
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatDuration(duration),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contrôles de lecture
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { isShuffle = !isShuffle }) {
                    Icon(
                        Icons.Default.Shuffle,
                        contentDescription = "Aléatoire",
                        tint = if (isShuffle)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = { viewModel.prev() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Précédent",
                        modifier = Modifier.size(32.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { viewModel.togglePlayPause() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Lecture",
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(
                    onClick = { viewModel.next() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Suivant",
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(
                    onClick = { repeatMode = (repeatMode + 1) % 3 }
                ) {
                    Icon(
                        when (repeatMode) {
                            0 -> Icons.Default.Repeat
                            1 -> Icons.Default.Repeat
                            else -> Icons.Default.RepeatOne
                        },
                        contentDescription = "Répéter",
                        tint = if (repeatMode > 0)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = { showSleepTimer = true }) {
                Icon(Icons.Default.Timer, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Minuteur")
            }

            if (showSleepTimer) {
                SleepTimerDialog(
                    onDismiss = { showSleepTimer = false },
                    onTimerFinished = { viewModel.pause() }
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucune musique sélectionnée")
            }
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}