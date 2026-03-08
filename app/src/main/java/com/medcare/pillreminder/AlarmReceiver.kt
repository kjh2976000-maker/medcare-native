package com.medcare.pillreminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medName = intent.getStringExtra("med_name") ?: "복약 시간"
        val medDosage = intent.getStringExtra("med_dosage") ?: ""
        val medId = intent.getStringExtra("med_id") ?: "1"

        // AlarmActivity 직접 실행 (전체화면 알람)
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("med_name", medName)
            putExtra("med_dosage", medDosage)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        context.startActivity(alarmIntent)

        // 알림도 함께 표시
        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingOpen = PendingIntent.getActivity(
            context, medId.hashCode(), openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("med_name", medName)
            putExtra("med_dosage", medDosage)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val fullScreenPending = PendingIntent.getActivity(
            context, medId.hashCode() + 1000, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, MedCareApp.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("💊 복약 시간입니다")
            .setContentText(if (medDosage.isNotEmpty()) "$medName - $medDosage" else medName)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingOpen)
            .setFullScreenIntent(fullScreenPending, true)
            .setOngoing(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(medId.hashCode(), notification)
    }
}
