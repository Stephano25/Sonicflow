package com.exemple.sonicflow.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.filled.*

@Composable
fun MiniPlayer(
    vm: PlayerViewModel,
    onClick: () -> Unit
) {

    val song = vm.currentSong

    AnimatedVisibility(
        visible = song != null,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {

        Surface(
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {

            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(song?.title ?: "")
                    Text(song?.artist ?: "")
                }

                IconButton(onClick = { vm.togglePlayPause() }) {
                    Icon(
                        if (vm.isPlaying())
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,
                        null
                    )
                }
            }
        }
    }
}

