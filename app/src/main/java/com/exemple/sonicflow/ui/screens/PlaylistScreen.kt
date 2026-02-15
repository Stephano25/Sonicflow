package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.data.room.Playlist
import kotlinx.coroutines.launch

@Composable
fun PlaylistScreen(viewModel: PlayerViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var newPlaylistName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            playlists = viewModel.getPlaylists()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("My Playlists", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = newPlaylistName,
            onValueChange = { newPlaylistName = it },
            label = { Text("New Playlist") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            if (newPlaylistName.isNotBlank()) {
                scope.launch {
                    viewModel.createPlaylist(newPlaylistName)
                    playlists = viewModel.getPlaylists()
                    newPlaylistName = ""
                }
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Create Playlist")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(playlists) { playlist ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            navController.navigate("playlistDetail/${playlist.id}/${playlist.name}")
                        }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(playlist.name, style = MaterialTheme.typography.titleMedium)
                        Text("Tap to view songs", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
