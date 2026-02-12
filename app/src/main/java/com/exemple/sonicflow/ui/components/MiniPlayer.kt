package com.exemple.sonicflow.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun MiniPlayer(viewModel: PlayerViewModel) {

    val current = viewModel.currentSong

    if (current != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(current.title)
                    Text(current.artist)
                }

                Button(onClick = { viewModel.togglePlayPause() }) {
                    Text("Play/Pause")
                }
            }
        }
    }
}
