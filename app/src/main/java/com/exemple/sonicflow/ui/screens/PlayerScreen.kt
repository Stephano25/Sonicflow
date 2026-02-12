package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val currentSong = viewModel.currentSong

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (currentSong != null) {
            Text(text = "Now Playing: ${currentSong.title}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Artist: ${currentSong.artist}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Album: ${currentSong.album}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.togglePlayPause() }) {
                Text("Play / Pause")
            }
        } else {
            Text("No song selected", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
