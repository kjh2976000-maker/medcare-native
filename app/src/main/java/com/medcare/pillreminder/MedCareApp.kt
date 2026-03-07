package com.medcare.pillreminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MedCareApp : Application() {

    companion object {
        const val CHANNEL_ID = "medcare_alarm"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "복약 알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
