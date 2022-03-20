package notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class AppNoti : Application() {
    companion object {
        const val CHANNEL_ID = "ALARM CHANNEL"
        const val CHANNEL_ID2 = "TIMER CHANNEL"
        const val CHANNEL_ID3 = "STOPWATCH CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // phai co khi ung dung chay android 8 tro len
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channel2 = NotificationChannel(
                CHANNEL_ID2,
                "Timer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channel3 = NotificationChannel(
                CHANNEL_ID3,
                "Stopwatch Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            channel2.setSound(null, null)
            channel3.setSound(null, null)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channel2)
            notificationManager.createNotificationChannel(channel3)
        }
    }
}