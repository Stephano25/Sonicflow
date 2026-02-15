package com.exemple.sonicflow.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.exemple.sonicflow.ui.screens.LibraryScreen
import com.exemple.sonicflow.ui.screens.PlayerScreen
import com.exemple.sonicflow.ui.screens.PlaylistScreen
import com.exemple.sonicflow.ui.screens.PlaylistDetailScreen
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.ui.components.MiniPlayer

@Composable
fun MainScreen(viewModel: PlayerViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column {
                // ✅ MiniPlayer visible partout
                MiniPlayer(viewModel, onClick = { navController.navigate("player") })
                // ✅ Barre de navigation en dessous
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
            composable("playlist") { PlaylistScreen(viewModel, navController) }

            // ✅ nouvelle route avec arguments
            composable("playlistDetail/{playlistId}/{playlistName}") { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")?.toLongOrNull() ?: 0L
                val playlistName = backStackEntry.arguments?.getString("playlistName") ?: "Playlist"
                PlaylistDetailScreen(viewModel, playlistId, playlistName)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentDestination == "library",
            onClick = { navController.navigate("library") },
            label = { Text("Library") },
            icon = { Icon(Icons.Default.LibraryMusic, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentDestination == "player",
            onClick = { navController.navigate("player") },
            label = { Text("Player") },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentDestination == "playlist",
            onClick = { navController.navigate("playlist") },
            label = { Text("Playlist") },
            icon = { Icon(Icons.Default.QueueMusic, contentDescription = null) }
        )
    }
}
