package com.exemple.sonicflow.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.exemple.sonicflow.MainActivity
import com.exemple.sonicflow.R
import com.exemple.sonicflow.player.PlayerManager

class MusicService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private lateinit var playerManager: PlayerManager

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "music_player_channel"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        playerManager = PlayerManager(this)
        mediaSession = MediaSession.Builder(this, playerManager.player).build()

        // Démarrer en mode foreground pour éviter que le système ne tue le service
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onDestroy() {
        mediaSession?.release()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for music playback"
                setSound(null, null)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SonicFlow")
            .setContentText("Lecture en cours...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
}