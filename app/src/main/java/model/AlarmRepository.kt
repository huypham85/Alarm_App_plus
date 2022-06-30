package model

import javax.inject.Inject

interface AlarmRepository {
    suspend fun getAlarms(): List<Alarm>

    suspend fun insert(alarm: Alarm)

    suspend fun update(alarm: Alarm)

    suspend fun delete(alarm: Alarm)

    suspend fun getAlarmWithId(id: Long): Alarm
}

class AlarmRepositoryImpl @Inject constructor(private val alarmDao: AlarmDao) : AlarmRepository {
    override suspend fun getAlarms(): List<Alarm> = alarmDao.getAlarms()

    override suspend fun insert(alarm: Alarm) {
        alarmDao.insertAlarm(alarm)
    }

    override suspend fun update(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    override suspend fun delete(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    override suspend fun getAlarmWithId(id: Long): Alarm {
        return alarmDao.getAlarmWithId(id)
    }
}