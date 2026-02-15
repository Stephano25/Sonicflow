package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.data.room.PlaylistSong
import com.exemple.sonicflow.data.room.toSong
import kotlinx.coroutines.launch

@Composable
fun PlaylistDetailScreen(viewModel: PlayerViewModel, playlistId: Long, playlistName: String) {
    val scope = rememberCoroutineScope()
    var songs by remember { mutableStateOf<List<PlaylistSong>>(emptyList()) }

    // ✅ Charger les chansons de la playlist
    LaunchedEffect(playlistId) {
        scope.launch {
            songs = viewModel.getSongsForPlaylist(playlistId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = playlistName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (songs.isEmpty()) {
            Text("No songs in this playlist", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn {
                items(songs) { song ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                // ✅ Lecture de la chanson
                                viewModel.play(song.toSong())
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(song.title, style = MaterialTheme.typography.titleMedium)
                                Text(song.artist, style = MaterialTheme.typography.bodyMedium)
                                Text(song.album, style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    viewModel.deleteSongFromPlaylist(song)
                                    songs = viewModel.getSongsForPlaylist(playlistId)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Song"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
