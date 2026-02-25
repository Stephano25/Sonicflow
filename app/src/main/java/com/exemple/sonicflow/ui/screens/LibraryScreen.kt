package com.exemple.sonicflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.exemple.sonicflow.ui.components.SongMenuItem
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: PlayerViewModel,
    navController: NavHostController
) {
    val songs by viewModel.songs.collectAsState()
    val scope = rememberCoroutineScope()
    var playlists by remember { mutableStateOf(emptyList<com.exemple.sonicflow.data.room.Playlist>()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        playlists = viewModel.getPlaylists()
    }

    val filteredSongs = if (searchQuery.isBlank()) {
        songs
    } else {
        songs.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.artist.contains(searchQuery, ignoreCase = true) ||
                    it.album.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bibliothèque") },
                actions = {
                    IconButton(onClick = { /* Recherche */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Rechercher")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                // Barre de recherche
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher une musique...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true
                )
            }

            items(filteredSongs) { song ->
                SongMenuItem(
                    song = song,
                    playlists = playlists,
                    onSongClick = {
                        viewModel.play(song)
                        navController.navigate("player")
                    },
                    onAddToPlaylist = { playlist ->
                        scope.launch {
                            viewModel.addSongToPlaylist(playlist.id, song)
                        }
                    },
                    onDelete = {
                        // Implémenter la suppression de la musique
                        // Note: Cela nécessite une permission spéciale
                    }
                )
            }
        }
    }
}