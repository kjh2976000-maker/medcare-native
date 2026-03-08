package com.medcare.pillreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.medcare.pillreminder.data.DataStore
import com.medcare.pillreminder.data.Medication
import java.util.Calendar

class AddMedicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)

        val etName = findViewById<EditText>(R.id.etMedName)
        val etDosage = findViewById<EditText>(R.id.etMedDosage)
        val timePicker = findViewById<TimePicker>(R.id.timePickerMed)
        val btnSave = findViewById<Button>(R.id.btnSaveMed)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        timePicker.setIs24HourView(true)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "약 이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hour = timePicker.hour
            val minute = timePicker.minute

            // 약 정보 저장
            val med = Medication(
                name = name,
                dosage = etDosage.text.toString().trim(),
                hour = hour,
                minute = minute,
                ampm = if (hour < 12) 0 else 1,
                days = listOf(0, 1, 2, 3, 4, 5, 6)
            )

            val dataStore = DataStore(this)
            val meds = dataStore.loadMedications()
            meds.add(med)
            dataStore.saveMedications(meds)

            // 알람 직접 등록
            scheduleAlarmDirectly(med, meds.size)

            Toast.makeText(this, "${name} 알람이 설정되었습니다 (${hour}시 ${minute}분)", Toast.LENGTH_LONG).show()
            finish()
        }

        btnCancel.setOnClickListener { finish() }
    }

    private fun scheduleAlarmDirectly(med: Medication, requestCode: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("med_name", med.name)
            putExtra("med_id", med.id)
            putExtra("med_dosage", med.dosage)
            putExtra("is_advance", false)
            putExtra("is_repeat", false)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, med.hour)
            set(Calendar.MINUTE, med.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // 이미 지난 시간이면 다음날로
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}
