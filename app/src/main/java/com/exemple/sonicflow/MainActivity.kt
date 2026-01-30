package com.exemple.sonicflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.exemple.sonicflow.ui.navigation.AppBottomNav
import com.exemple.sonicflow.ui.navigation.AppNavHost
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.viewmodel.PlaylistViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val playerVm: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory(application))
            val playlistVm: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory(application))

            androidx.compose.material3.Scaffold(
                bottomBar = { AppBottomNav(navController) }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    playerViewModel = playerVm,
                    playlistViewModel = playlistVm,
                    modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                )
            }
        }
    }
}
