package com.exemple.sonicflow.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun LibraryScreen(vm: PlayerViewModel, navController: NavController) {
    var query by remember { mutableStateOf("") }
    val songs by vm.songs.collectAsState()

    val filteredSongs = songs.filter {
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
            itemsIndexed(filteredSongs) { index, song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            vm.playAll(filteredSongs, index)
                            navController.navigate("nowplaying")
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(song.title, style = MaterialTheme.typography.bodyLarge)
                        Text(song.artist, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
