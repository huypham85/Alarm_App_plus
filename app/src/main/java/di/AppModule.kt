package di

import Constants.DATABASE_NAME
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import model.AlarmDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AlarmDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideAlarmDao(
        database: AlarmDatabase
    ) = database.alarmDao()

    @Singleton
    @Provides
    fun provideTextToSpeech(@ApplicationContext context: Context) = TextToSpeech(context, TextToSpeech.OnInitListener {

    })
}