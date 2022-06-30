package service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmRepository
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmService :
    Service() {
    @Inject
    lateinit var alarmRepository: AlarmRepository
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
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