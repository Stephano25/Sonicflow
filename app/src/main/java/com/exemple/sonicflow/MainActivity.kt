package com.exemple.sonicflow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.exemple.sonicflow.service.MusicService
import com.exemple.sonicflow.ui.navigation.MainScreen
import com.exemple.sonicflow.viewmodel.PlayerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PlayerViewModel by viewModels()
    private var hasPermission by mutableStateOf(false)

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            viewModel.loadSongs()
            startMusicService()
        } else {
            Toast.makeText(
                this,
                "Permission requise pour lire la musique",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermission()

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF8E2DE2),
                    secondary = Color(0xFF4A00E0)
                )
            ) {
                LaunchedEffect(hasPermission) {
                    if (hasPermission) {
                        viewModel.loadSongs()
                    }
                }

                MainScreen(viewModel)
            }
        }
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                hasPermission = true
                viewModel.loadSongs()
                startMusicService()
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    private fun startMusicService() {
        val intent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}