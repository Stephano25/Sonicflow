package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.data.model.Song

@Composable
fun PlaylistScreen(viewModel: PlayerViewModel) {
    val songs = viewModel.songs // pour l'instant, même liste que Library

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "My Playlist",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn {
            items(songs) { song: Song ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { viewModel.play(song) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = song.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = song.artist, style = MaterialTheme.typography.bodyMedium)
                        Text(text = song.album, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
