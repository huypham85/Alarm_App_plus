package model

class AlarmRepository(private var alarmDao: AlarmDao) {


    // suspend fun getData() : LiveData<List<Alarm>> = alarmDao.getAlarms()

    suspend fun getAlarms(): List<Alarm> = alarmDao.getAlarms()

    //suspend fun getAlarms() = alarmDao.getAlarms()
    suspend fun insert(alarm: Alarm) {
        alarmDao.insertAlarm(alarm)
    }

    suspend fun update(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    fun getAlarmWithId(id: Long): Alarm {
        return alarmDao.getAlarmWithId(id)
    }
}