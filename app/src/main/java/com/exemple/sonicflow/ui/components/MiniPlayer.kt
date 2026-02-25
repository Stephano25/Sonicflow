package com.exemple.sonicflow.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun MiniPlayer(
    vm: PlayerViewModel,
    onClick: () -> Unit
) {
    val song by vm.currentSong.collectAsState()
    val isPlaying by vm.isPlaying.collectAsState()

    AnimatedVisibility(
        visible = song != null,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Surface(
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = song?.title ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = song?.artist ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { vm.togglePlayPause() }) {
                    Icon(
                        if (isPlaying)
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
            }
        }
    }
}