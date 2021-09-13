package model

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

class AlarmRepository(private var alarmDao: AlarmDao) {
    var liveDataAlarms: LiveData<List<Alarm>> = alarmDao.getAlarms()
    fun insert(alarm: Alarm){
        thread {
            alarmDao.insertAlarm(alarm)
        }
        AlarmDatabase.databaseWriteExecutor.execute {
            Log.e("Alarm Repo","insert")
        }
    }
    fun update(alarm: Alarm){
        AlarmDatabase.databaseWriteExecutor.execute {
            alarmDao.updateAlarm(alarm)
        }
    }
}