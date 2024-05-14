package com.tchaikovsky.androidcomponents

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(MEDIA_CHANNEL_ID, MEDIA_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else
            Log.d("@@@", "Channel not use")
    }

    companion object{
        private const val MEDIA_CHANNEL_NAME = "Media channel"
        const val MEDIA_CHANNEL_ID = "IdMediaPlayer"
    }
}