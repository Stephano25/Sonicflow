package com.exemple.sonicflow.ui.player

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerProgress(vm: PlayerViewModel) {
    val progress by vm.progress.collectAsState()   // Float
    val duration by vm.duration.collectAsState()   // Float

    Slider(
        value = if (duration > 0f) progress / duration else 0f,
        onValueChange = { fraction ->
            val newPosition = (fraction * duration).toLong() // ✅ cast explicite en Long
            vm.seekTo(newPosition)
        }
    )
}
