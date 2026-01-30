package com.exemple.sonicflow.ui.player

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun PlayerProgress(vm: PlayerViewModel) {
    val progress = vm.progress
    val duration = vm.duration

    Slider(
        value = if (duration > 0) progress / duration else 0f,
        onValueChange = { fraction ->
            vm.seekTo(fraction * duration)
        }
    )
}
