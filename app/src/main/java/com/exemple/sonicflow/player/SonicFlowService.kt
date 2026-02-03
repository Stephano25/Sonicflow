package com.exemple.sonicflow.player

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.exemple.sonicflow.MainActivity

class SonicFlowService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()
        val playerManager = PlayerManager(this)

        val sessionActivity = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        mediaSession = MediaSession.Builder(this, playerManager.getPlayer())
            .setSessionActivity(sessionActivity)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }
}
