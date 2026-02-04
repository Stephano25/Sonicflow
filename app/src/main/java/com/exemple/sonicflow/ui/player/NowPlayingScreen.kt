package com.exemple.sonicflow.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.R
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*

@Composable
fun NowPlayingScreen(vm: PlayerViewModel) {
    val currentSong by vm.currentSong.collectAsState()
    val isPlaying by vm.isPlaying.collectAsState()
    val progress by vm.progress.collectAsState()
    val duration by vm.duration.collectAsState()

    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // ✅ Rotation infinie du disque
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            scope.launch {
                while (true) {
                    rotation.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(10000, easing = LinearEasing)
                    )
                    rotation.snapTo(0f)
                }
            }
        } else {
            rotation.snapTo(0f)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Disque qui tourne ---
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_round),
            contentDescription = "Album Art",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .graphicsLayer { rotationZ = rotation.value }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Titre + artiste ---
        Text(
            currentSong?.title ?: "Aucune chanson",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            currentSong?.artist ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Barre de progression ---
        if (duration > 0f) {
            Slider(
                value = progress.coerceIn(0f, duration),
                onValueChange = { vm.seekTo(it.toLong()) },
                valueRange = 0f..duration,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Boutons de contrôle ---
        PlayerControls(
            isPlaying = isPlaying,
            onPlayPause = { vm.playPause() },
            onNext = { vm.next() },
            onPrevious = { vm.previous() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Waveform ---
        WaveformVisualizer(amplitudes = listOf(10, 30, 50, 70, 40, 20))
    }
}

@Composable
fun WaveformVisualizer(amplitudes: List<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        amplitudes.forEach { amp ->
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(amp.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
            )
        }
    }
}
