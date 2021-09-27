package model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Alarm::class], version = 6, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase(){
    abstract fun alarmDao() : AlarmDao
    companion object{
        @Volatile
        private var instance: AlarmDatabase? = null
        // singleton
        fun getInstance(context: Context): AlarmDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AlarmDatabase {
            return Room.databaseBuilder(
                    context,
                    AlarmDatabase::class.java,
                    "alarm_database.db"
            ).fallbackToDestructiveMigration().build()
        }
    }

}