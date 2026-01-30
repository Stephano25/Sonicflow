package com.exemple.sonicflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exemple.sonicflow.ui.navigation.AppBottomNav
import com.exemple.sonicflow.ui.navigation.AppNavHost
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.viewmodel.PlaylistViewModel
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val playerVm: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory(application))
            val playlistVm: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory(application))

            Scaffold(
                bottomBar = { AppBottomNav(navController) }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    playerViewModel = playerVm,
                    playlistViewModel = playlistVm,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
