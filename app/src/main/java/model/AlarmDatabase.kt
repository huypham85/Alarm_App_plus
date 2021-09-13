package model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Alarm::class], version = 3, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase(){
    abstract fun alarmDao() : AlarmDao
    companion object{
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor : ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
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
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                            context.applicationContext,
//                            AlarmDatabase::class.java,
//                            "alarm_database"
//                    )
//                            .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
    }

}