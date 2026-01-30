package com.exemple.sonicflow.ui.main

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
fun MainScreen(vm: PlayerViewModel) {
    val songs by vm.songs.collectAsState()
    val isPlaying by vm.isPlaying.collectAsState()
    val currentSong by vm.currentSong.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lecteur SonicFlow", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Chanson en cours
        currentSong?.let { song ->
            Text("En cours : ${song.title} - ${song.artist}", style = MaterialTheme.typography.bodyLarge)
        } ?: Text("Aucune chanson en cours", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Contrôles du lecteur
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { vm.previous() }) { Text("⏮️") }
            Button(onClick = { vm.playPause() }) { Text(if (isPlaying) "⏸️ Pause" else "▶️ Lecture") }
            Button(onClick = { vm.next() }) { Text("⏭️") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Liste des chansons
        Text("Bibliothèque", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(songs) { song ->
                SongItem(song = song, onPlay = { vm.play(song) })
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onPlay: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(song.title, style = MaterialTheme.typography.bodyLarge)
            Text(song.artist, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Button(onClick = onPlay) {
            Text("▶️")
        }
    }
}
