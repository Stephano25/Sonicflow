package com.exemple.sonicflow.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.exemple.sonicflow.ui.screens.*
import com.exemple.sonicflow.ui.components.MiniPlayer
import com.exemple.sonicflow.viewmodel.PlayerViewModel

@Composable
fun AppNavigation(viewModel: PlayerViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(viewModel)
                NavigationBar {
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("library") },
                        icon = {},
                        label = { Text("Library") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("player") },
                        icon = {},
                        label = { Text("Player") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("playlist") },
                        icon = {},
                        label = { Text("Playlist") }
                    )
                }
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
