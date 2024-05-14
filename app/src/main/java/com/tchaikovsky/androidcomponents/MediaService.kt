package com.tchaikovsky.androidcomponents

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tchaikovsky.androidcomponents.App.Companion.MEDIA_CHANNEL_ID

class MediaService : Service() {

    private val player: MediaPlayer by lazy {
        MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI).apply {
            isLooping = true
        }
    }

    private val stopPendingIntent: PendingIntent by lazy {
        val intent = Intent(this, MediaService::class.java).putExtra(
            KEY_FOR_MEDIA_SERVICE_INTENT,
            getString(R.string.stop)
        )
        PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val playPendingIntent: PendingIntent by lazy {
        val intent = Intent(this, MediaService::class.java).putExtra(
            KEY_FOR_MEDIA_SERVICE_INTENT,
            getString(R.string.play)
        )
        PendingIntent.getService(
            this,
            1,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val closePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, MediaService::class.java).putExtra(
            KEY_FOR_MEDIA_SERVICE_INTENT,
            getString(R.string.close)
        )
        PendingIntent.getService(
            this,
            2,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val notification: Notification by lazy {
        NotificationCompat.Builder(this, MEDIA_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_music_note_24)
            .setContentTitle(getString(R.string.play))
            .setContentInfo(getString(R.string.play_info))
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setAutoCancel(true)
            .setColor(getColor(R.color.green))
            .addAction(R.drawable.baseline_stop_24, getString(R.string.stop), stopPendingIntent)
            .addAction(R.drawable.baseline_play_arrow_24, getString(R.string.play), playPendingIntent)
            .addAction(R.drawable.baseline_close_24, getString(R.string.close), closePendingIntent)
            .build()
    }

    override fun onCreate() {
        Log.d("@@@", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getStringExtra(KEY_FOR_MEDIA_SERVICE_INTENT)
        Log.d("@@@", "onStartCommand flags $flags startId $startId message $data")
        when (data){
            getString(R.string.stop) -> player.pause()
            getString(R.string.play) -> {
                if (startId==1){
                    player.start()
                    startForeground(FOREGROUND_MEDIA_SERVICE_ID, notification)
                } else
                    player.start()
            }
            getString(R.string.close) -> stopSelf()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        Log.d("@@@", "onDestroy")
        player.release()
        super.onDestroy()
    }

    companion object {
        private const val FOREGROUND_MEDIA_SERVICE_ID = 45
        internal const val KEY_FOR_MEDIA_SERVICE_INTENT = "KeyForMediaServiceIntent"
    }
}