package com.tchaikovsky.androidcomponents

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class MediaService : Service() {

    private val player: MediaPlayer by lazy {
        MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI).apply {
            isLooping = true
        }
    }

    override fun onCreate() {
        Log.d("@@@", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getStringExtra(SecondActivity.KEY_FOR_MEDIA_SERVICE)
        Log.d("@@@", "onStartCommand flags $flags startId $startId message $data")
        player.start()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        Log.d("@@@", "onDestroy")
        player.stop()
        super.onDestroy()
    }
}