package com.exemple.sonicflow

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.exemple.sonicflow.ui.navigation.AppBottomNav
import com.exemple.sonicflow.ui.navigation.AppNavHost
import com.exemple.sonicflow.viewmodel.PlayerViewModel
import com.exemple.sonicflow.viewmodel.PlaylistViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Demande de permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.READ_MEDIA_AUDIO
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            }
        } else {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            }
        }

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
