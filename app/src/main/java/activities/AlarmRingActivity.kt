package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.alarmapp.databinding.ActivityAlarmRingBinding
import fragments.AddAlarmFragment
import model.Alarm
import service.AlarmService
import viewModel.AlarmListViewModel
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class AlarmRingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmRingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTimeAndLabel()

        // onClick
        binding.btnStop.setOnClickListener { // if stop, stop service => stop Alarm
            val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            finish() // tat activity
        }
        binding.btnSnooze.setOnClickListener {
            // khi snooze, alarm se tat vao luc do va lap lai bao thuc sau 10p, => tao 1 bao thuc moi sau 10p
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 2)

            val newAlarm = Alarm(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    recurring = true,
                    monday = false,
                    tuesday = false,
                    wednesday = false,
                    thursday = false,
                    friday = false,
                    saturday = false,
                    sunday = false,
                    label = "Snooze",
                    isOn = true,
                    System.currentTimeMillis()
//                    AlarmListViewModel.index++
            )
            newAlarm.schedule(applicationContext) // tao 1 bao thuc moi vao 10 phut sau

            // ket thuc activity ring
            val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            finish()

        }

    }

    private fun initTimeAndLabel() {
        val intent:Intent = intent // lay ve cai intent ma bat dau cai activity nay
        val alarmHour = intent.getIntExtra("HOUR",0)
        val alarmMinute = intent.getIntExtra("MINUTE",0)
        val alarmTime = "$alarmHour : $alarmMinute"
        binding.txtALarmTime.text = alarmTime

        intent.getStringExtra("LABEL")?.let { Log.e("Label", it) }
        val alarmLabel = intent.getStringExtra("LABEL")

        binding.txtAlarmLabel.text = alarmLabel
    }
}