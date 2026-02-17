package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.data.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    viewModel: PlayerViewModel,
    navController: NavController,
    playlistId: Long,
    playlistName: String
) {

    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(playlistId) {
        songs = viewModel.getSongsFromPlaylist(playlistId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlistName) }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            items(songs) { song ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.playSong(song)

                            // 🔥 Naviguer vers PlayerScreen
                            navController.navigate("player") {
                                launchSingleTop = true
                            }
                        }
                        .padding(16.dp)
                ) {

                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = song.artist,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Divider()
            }
        }
    }
}
