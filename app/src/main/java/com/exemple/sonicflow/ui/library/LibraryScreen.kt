package com.exemple.sonicflow.ui.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun LibraryScreen(vm: PlayerViewModel) {
    var query by remember { mutableStateOf("") }
    val songs = vm.songs.filter {
        it.title.contains(query, ignoreCase = true) || it.artist.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Recherche par titre ou artiste") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(songs) { song ->
                Text(song.title + " - " + song.artist, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
