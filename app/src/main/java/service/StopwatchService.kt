package service

import activities.MainActivity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapp.R
import notifications.AppNoti
import java.util.*
import kotlin.math.roundToInt

class StopwatchService : Service() {

    companion object {
        const val TIMER_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time), 0, 10)
        return START_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private fun sendNotification(time: Double) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, AppNoti.CHANNEL_ID3)
            .setContentTitle("Stopwatch is running")
            .setContentText(updateNotification(time))
            .setSmallIcon(R.drawable.ic_stopwatch)
            .setContentIntent(pendingIntent)
            .setSound(null)
            .build()
        startForeground(3, notification)
    }

    private fun updateNotification(time: Double): String {
        val resultInt = time.roundToInt()
        val millis = resultInt % 100
        val seconds = resultInt / 100 % 60
        val minutes = resultInt / 100 / 60 % 60
        return makeTimeString(minutes, seconds, millis)
    }

    private fun makeTimeString(minutes: Int, seconds: Int, millis: Int): String {
        return String.format("%02d:%02d:%02d", minutes, seconds, millis)
    }

    private inner class TimeTask(private var time: Double) : TimerTask() {
        override fun run() {
            val intent = Intent(TIMER_UPDATED)
            time++
            intent.putExtra(TIME_EXTRA, time)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            sendNotification(time)

            val sharedPreferences = getSharedPreferences("stopWatchPref", MODE_PRIVATE)
            sharedPreferences?.edit()?.putFloat("timeRunning", time.toFloat())?.apply()
//            val timeRunning = sharedPreferences?.getFloat("timeRunning", 0.0F)
//            Log.e("Time push",timeRunning.toString())
        }
    }

}