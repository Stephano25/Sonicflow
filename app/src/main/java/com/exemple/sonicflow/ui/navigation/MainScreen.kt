package com.exemple.sonicflow.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.exemple.sonicflow.ui.screens.LibraryScreen
import com.exemple.sonicflow.ui.screens.PlayerScreen
import com.exemple.sonicflow.ui.screens.PlaylistScreen
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.ui.components.MiniPlayer

@Composable
fun MainScreen(viewModel: PlayerViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(viewModel) // ✅ version correcte
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "library",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("library") { LibraryScreen(viewModel) }
            composable("player") { PlayerScreen(viewModel) }
            composable("playlist") { PlaylistScreen(viewModel) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("library") },
            label = { Text("Library") },
            icon = { Icon(Icons.Default.LibraryMusic, null) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("player") },
            label = { Text("Player") },
            icon = { Icon(Icons.Default.PlayArrow, null) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("playlist") },
            label = { Text("Playlist") },
            icon = { Icon(Icons.Default.QueueMusic, null) }
        )
    }
}
