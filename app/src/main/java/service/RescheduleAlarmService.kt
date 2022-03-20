package service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmDatabase
import model.AlarmRepository

class RescheduleAlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmDao = AlarmDatabase.getInstance(applicationContext).alarmDao()
        val alarmRepository = AlarmRepository(alarmDao)
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.getAlarms().let {
                for (alarm: Alarm in it) {
                    if (alarm.isOn) {
                        alarm.schedule(applicationContext)
                    }
                }
                stopSelf()
            }
        }
        return START_STICKY
    }

}