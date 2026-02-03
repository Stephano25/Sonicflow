package com.exemple.sonicflow.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun NowPlayingScreen(vm: PlayerViewModel) {
    val currentSong by vm.currentSong.collectAsState()
    val isPlaying by vm.isPlaying.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pochette (placeholder si pas d'image)
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_round),
            contentDescription = "Album Art",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Titre + artiste
        Text(
            text = currentSong?.title ?: "Aucune chanson en cours",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = currentSong?.artist ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Barre de progression
        PlayerProgress(vm)

        Spacer(modifier = Modifier.height(24.dp))

        // Boutons de contrôle
        PlayerControls(
            isPlaying = isPlaying,
            onPlayPause = { vm.playPause() },
            onNext = { vm.next() },
            onPrevious = { vm.previous() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Visualisation waveform
        currentSong?.let {
            WaveformVisualizer(amplitudes = listOf(10, 30, 50, 70, 40, 20)) // exemple
        }
    }
}
