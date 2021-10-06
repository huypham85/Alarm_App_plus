package service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmDatabase
import model.AlarmRepository

class RescheduleAlarmService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var alarmDao = AlarmDatabase.getInstance(applicationContext).alarmDao()
        var alarmRepository = AlarmRepository(alarmDao)
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.getAlarms().let {
                if (it != null) {
                    for(alarm: Alarm in it){
                        if(alarm.isOn){
                            alarm.schedule(applicationContext)
                        }
                    }
                }
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}