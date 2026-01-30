package com.exemple.sonicflow.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.player.WaveformExtractor
import com.exemple.sonicflow.ui.player.PlayerControls
import com.exemple.sonicflow.ui.player.PlayerVisualizer
import com.exemple.sonicflow.ui.player.WaveformVisualizer
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun MainScreen(vm: PlayerViewModel) {
    val isPlaying = vm.isPlaying.collectAsState().value
    val songs = vm.songs

    val amplitudes = remember { mutableStateListOf<Int>() }

    LaunchedEffect(Unit) {
        val song = vm.songs.firstOrNull()
        if (song != null) {
            amplitudes.addAll(WaveformExtractor.extractAmplitudes(LocalContext.current, song.uri))
        }
    }

    WaveformVisualizer(amplitudes)

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Playlist", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(songs) { song ->
                        SongItem(song = song, onClick = { vm.play(song) })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                PlayerVisualizer()
                PlayerControls(
                    isPlaying = isPlaying,
                    onPlayPause = { vm.playPause() },
                    onNext = { vm.next() },
                    onPrevious = { vm.previous() }
                )
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(song.title, style = MaterialTheme.typography.bodyLarge)
        Text(song.artist, style = MaterialTheme.typography.bodyMedium)
    }
}
