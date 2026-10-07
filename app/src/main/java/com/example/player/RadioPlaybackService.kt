package com.example.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

class RadioPlaybackService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START_OR_UPDATE
        if (action == ACTION_STOP) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        val stationName = intent?.getStringExtra(EXTRA_STATION_NAME) ?: "Radio Indonesia"
        val stationRegion = intent?.getStringExtra(EXTRA_STATION_REGION) ?: "Suara Nusantara"
        val isPlaying = intent?.getBooleanExtra(EXTRA_IS_PLAYING, true) ?: true

        val notification = buildNotification(stationName, stationRegion, isPlaying)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (_: Throwable) {
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pemutaran Radio Indonesia",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Menampilkan kontrol siaran radio saat diputar di latar belakang"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(
        stationName: String,
        stationRegion: String,
        isPlaying: Boolean
    ): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingOpenIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val statusText = if (isPlaying) "Sedang Mengudara • $stationRegion" else "Dijeda • $stationRegion"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(stationName)
            .setContentText(statusText)
            .setSubText("Radio Indonesia")
            .setContentIntent(pendingOpenIntent)
            .setOngoing(isPlaying)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "radio_indonesia_playback_channel"
        private const val NOTIFICATION_ID = 1001

        const val ACTION_START_OR_UPDATE = "com.example.player.action.START_OR_UPDATE"
        const val ACTION_STOP = "com.example.player.action.STOP"

        private const val EXTRA_STATION_NAME = "extra_station_name"
        private const val EXTRA_STATION_REGION = "extra_station_region"
        private const val EXTRA_IS_PLAYING = "extra_is_playing"

        fun updatePlaybackNotification(
            context: Context,
            stationName: String,
            stationRegion: String,
            isPlaying: Boolean
        ) {
            try {
                val intent = Intent(context, RadioPlaybackService::class.java).apply {
                    action = ACTION_START_OR_UPDATE
                    putExtra(EXTRA_STATION_NAME, stationName)
                    putExtra(EXTRA_STATION_REGION, stationRegion)
                    putExtra(EXTRA_IS_PLAYING, isPlaying)
                }
                ContextCompat.startForegroundService(context, intent)
            } catch (_: Throwable) {
            }
        }

        fun stopPlaybackService(context: Context) {
            try {
                val intent = Intent(context, RadioPlaybackService::class.java).apply {
                    action = ACTION_STOP
                }
                context.stopService(intent)
            } catch (_: Throwable) {
            }
        }
    }
}
