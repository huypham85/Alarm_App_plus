package broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import model.AlarmRepository
import service.AlarmService
import service.RescheduleAlarmService
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: AlarmRepository
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Toast.makeText(context, "Alarm is rescheduled", Toast.LENGTH_LONG).show()
            startRescheduleAlarmService(context)
        } else {
            if (!intent.getBooleanExtra("RECURRING", false)) { //neu bao thuc ko lap lai
                val id = intent.getLongExtra("ID", 1)
                CoroutineScope(Dispatchers.IO).launch {
                    val alarm = async { repository.getAlarmWithId(id) }
                    alarm.await().apply {
                        isOn = false
                        repository.update(this)
                    }
                }
                startAlarmService(context, intent)
            } else {
                if (todayHasAlarm(intent)) { // neu co bao thuc trong ngay thi moi start service (dung cho truong hop bao thuc lap lai nhieu ngay)
                    startAlarmService(context, intent)
                }
            }
        }
    }

    private fun todayHasAlarm(intent: Intent): Boolean { // check if today has alarm
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val today: Int = calendar.get(Calendar.DAY_OF_WEEK)
        when (today) {
            Calendar.MONDAY -> {
                if (intent.getBooleanExtra("MONDAY", false)) return true
                return false
            }
            Calendar.TUESDAY -> {
                if (intent.getBooleanExtra("TUESDAY", false)) return true
                return false
            }
            Calendar.WEDNESDAY -> {
                if (intent.getBooleanExtra("WEDNESDAY", false)) return true
                return false
            }
            Calendar.THURSDAY -> {
                if (intent.getBooleanExtra("THURSDAY", false)) return true
                return false
            }
            Calendar.FRIDAY -> {
                if (intent.getBooleanExtra("FRIDAY", false)) return true
                return false
            }
            Calendar.SATURDAY -> {
                if (intent.getBooleanExtra("SATURDAY", false)) return true
                return false
            }
            Calendar.SUNDAY -> {
                if (intent.getBooleanExtra("SUNDAY", false)) return true
                return false
            }
        }
        return false
    }

    private fun startRescheduleAlarmService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java).apply {
            putExtra("LABEL", intent.getStringExtra("LABEL"))
            putExtra("HOUR", intent.getIntExtra("HOUR", 0))
            putExtra("MINUTE", intent.getIntExtra("MINUTE", 0))
            putExtra("ID", intent.getLongExtra("ID", 0))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}