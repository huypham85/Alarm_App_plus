package model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.Update

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm:Alarm)

    @Query("SELECT * from alarm_table") // tra ve 1 list bao thuc dc sap xep theo thoi gian tao
    fun getAlarms(): LiveData<List<Alarm>>

    @Update
    fun updateAlarm(alarm: Alarm)
}