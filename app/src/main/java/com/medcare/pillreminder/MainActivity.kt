package com.medcare.pillreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNotificationPermission()

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val btnSave = findViewById<Button>(R.id.btnSave)

        timePicker.setIs24HourView(true)

        btnSave.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            AlarmHelper(this).scheduleAlarm(hour, minute)
            Toast.makeText(
                this,
                "${hour}시 ${minute}분 알람이 설정되었습니다",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}
