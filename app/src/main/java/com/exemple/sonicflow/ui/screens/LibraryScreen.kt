package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.exemple.sonicflow.utils.AlbumArtUtil
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    viewModel: PlayerViewModel,
    navController: NavHostController
) {

    val songs = viewModel.songs
    val scope = rememberCoroutineScope()
    var playlists by remember { mutableStateOf(emptyList<com.exemple.sonicflow.data.room.Playlist>()) }

    LaunchedEffect(Unit) {
        playlists = viewModel.getPlaylists()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        items(songs) { song ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        viewModel.play(song)
                        navController.navigate("player")
                    }
            ) {

                Row(modifier = Modifier.padding(12.dp)) {

                    AsyncImage(
                        model = AlbumArtUtil.getAlbumArtUri(song.id),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(song.title)
                        Text(song.artist)
                    }

                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, null)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        playlists.forEach { playlist ->
                            DropdownMenuItem(
                                text = { Text("Add to ${playlist.name}") },
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