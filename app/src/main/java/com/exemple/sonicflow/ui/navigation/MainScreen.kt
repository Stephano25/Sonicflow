package com.exemple.sonicflow.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.exemple.sonicflow.ui.screens.*
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.ui.components.MiniPlayer

@Composable
fun MainScreen(viewModel: PlayerViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(
                    vm = viewModel,
                    onClick = {
                        navController.navigate("player") {
                            launchSingleTop = true
                        }
                    }
                )
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        // ✅ NavHost simple et propre
        NavHost(
            navController = navController,
            startDestination = "library",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("library") { LibraryScreen(viewModel) }
            composable("player") { PlayerScreen(viewModel) }
            composable("playlist") { PlaylistScreen(viewModel, navController) }

            composable("playlistDetail/{playlistId}/{playlistName}") { backStackEntry ->
                val playlistId =
                    backStackEntry.arguments?.getString("playlistId")?.toLongOrNull() ?: 0L
                val playlistName =
                    backStackEntry.arguments?.getString("playlistName") ?: "Playlist"

                PlaylistDetailScreen(
                    viewModel = viewModel,
                    navController = navController,
                    playlistId = playlistId,
                    playlistName = playlistName
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "library",
            onClick = {
                navController.navigate("library") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            label = { Text("Library") },
            icon = { Icon(Icons.Default.LibraryMusic, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentRoute == "player",
            onClick = {
                navController.navigate("player") {
                    launchSingleTop = true
                }
            },
            label = { Text("Player") },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentRoute == "playlist",
            onClick = {
                navController.navigate("playlist") {
                    launchSingleTop = true
                }
            },
            label = { Text("Playlist") },
            icon = { Icon(Icons.Default.QueueMusic, contentDescription = null) }
        )
    }
}
