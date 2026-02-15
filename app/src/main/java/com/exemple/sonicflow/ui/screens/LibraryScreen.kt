package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.room.Playlist
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(viewModel: PlayerViewModel) {
    val songs: List<Song> = viewModel.songs.toList()
    val scope = rememberCoroutineScope()

    // ✅ playlists persistantes depuis Room
    var playlists by remember { mutableStateOf(emptyList<Playlist>()) }

    // Charger les playlists existantes
    LaunchedEffect(Unit) {
        scope.launch {
            playlists = viewModel.getPlaylists()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (songs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No songs found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(songs) { song ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(song.title, style = MaterialTheme.typography.titleMedium)
                            Text(song.artist, style = MaterialTheme.typography.bodyMedium)
                            Text(song.album, style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                // ✅ Bouton Play
                                Button(onClick = { viewModel.play(song) }) {
                                    Text("Play")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // ✅ Bouton Add to Playlist
                                var expanded by remember { mutableStateOf(false) }
                                Box {
                                    Button(onClick = { expanded = true }) {
                                        Text("Add to Playlist")
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        playlists.forEach { playlist: Playlist ->
                                            DropdownMenuItem(
                                                text = { Text(playlist.name) },
                                                onClick = {
                                                    scope.launch {
                                                        viewModel.addSongToPlaylist(playlist.id, song)
                                                    }
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
