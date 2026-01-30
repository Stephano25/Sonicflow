package com.exemple.sonicflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.exemple.sonicflow.ui.main.MainScreen
import com.exemple.sonicflow.ui.playlist.PlaylistScreen
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.viewmodel.PlaylistViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    playlistViewModel: PlaylistViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            MainScreen(playerViewModel)
        }
        composable("playlists") {
            PlaylistScreen(playlistViewModel)
        }
    }
}
