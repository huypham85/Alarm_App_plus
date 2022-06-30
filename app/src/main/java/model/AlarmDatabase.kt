package model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 6, exportSchema = true)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}