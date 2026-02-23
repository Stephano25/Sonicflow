package com.exemple.sonicflow.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.exemple.sonicflow.ui.components.MiniPlayer
import com.exemple.sonicflow.ui.screens.*
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import androidx.navigation.NavHostController

@Composable
fun MainScreen(viewModel: PlayerViewModel) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "library",
            modifier = Modifier.padding(padding)
        ) {

            composable("library") {
                LibraryScreen(viewModel, navController)
            }

            composable("player") {
                PlayerScreen(viewModel)
            }

            composable("playlist") {
                PlaylistScreen(viewModel, navController)
            }

            composable(
                "playlistDetail/{id}/{name}"
            ) { backStackEntry ->

                val id =
                    backStackEntry.arguments?.getString("id")?.toLong() ?: 0L

                val name =
                    backStackEntry.arguments?.getString("name") ?: ""

                PlaylistDetailScreen(
                    viewModel,
                    navController,
                    id,
                    name
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "library",
            onClick = {
                navController.navigate("library") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.LibraryMusic, null) },
            label = { Text("Library") }
        )

        NavigationBarItem(
            selected = currentRoute == "player",
            onClick = {
                navController.navigate("player") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.PlayArrow, null) },
            label = { Text("Player") }
        )

        NavigationBarItem(
            selected = currentRoute == "playlist",
            onClick = {
                navController.navigate("playlist") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.QueueMusic, null) },
            label = { Text("Playlist") }
        )
    }
}