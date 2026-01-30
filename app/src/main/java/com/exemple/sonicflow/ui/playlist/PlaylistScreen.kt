package com.exemple.sonicflow.ui.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.data.model.PlaylistWithSongs
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.viewmodel.PlaylistViewModel

@Composable
fun PlaylistScreen(vm: PlaylistViewModel) {
    val playlists by vm.playlists.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Playlists", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(playlists) { playlistWithSongs ->
                PlaylistItem(
                    playlistWithSongs = playlistWithSongs,
                    onSongClick = { song -> vm.removeSongFromPlaylist(playlistWithSongs.playlist.playlistId, song) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour créer une nouvelle playlist
        var newPlaylistName by remember { mutableStateOf("") }
        OutlinedTextField(
            value = newPlaylistName,
            onValueChange = { newPlaylistName = it },
            label = { Text("Nom de la playlist") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (newPlaylistName.isNotBlank()) {
                    vm.createPlaylist(newPlaylistName)
                    newPlaylistName = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Créer Playlist")
        }
    }
}

@Composable
fun PlaylistItem(
    playlistWithSongs: PlaylistWithSongs,
    onSongClick: (Song) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(playlistWithSongs.playlist.name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        playlistWithSongs.songs.forEach { song ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSongClick(song) }
                    .padding(4.dp)
            ) {
                Text(song.title, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Text(song.artist, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
