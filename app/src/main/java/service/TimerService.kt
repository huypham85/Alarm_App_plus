package service

import activities.MainActivity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapp.R
import notifications.AppNotification.Companion.CHANNEL_ID2

class TimerService : Service() {


    private lateinit var countDownTimer: CountDownTimer
    var intentService = Intent(COUNTDOWN)
    private lateinit var mediaPlayer: MediaPlayer

    //use to filter intent
    companion object {
        const val COUNTDOWN = "backgroundtimer"
        const val TIME_OUT = "time_out"
        const val STOP = "stop_service"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.timer)
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (STOP == intent?.action) {
            stopForeground(true)
            stopSelf()
        } else {
            startTimer(intent)
        }
        return START_STICKY
    }

    private fun startTimer(intent: Intent?) {
        val time = intent?.getLongExtra("New time", 0)
        countDownTimer = object : CountDownTimer(time?.times(1000)!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                intentService.putExtra("countdown", millisUntilFinished / 1000)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentService)
                sendNotification(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                val intentTimeOut = Intent(TIME_OUT)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentTimeOut)
                //stopSelf()
            }

        }.start()
    }

    private fun sendNotification(time: Long) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (time > 0) { // neu van con thoi gian
            val notification = NotificationCompat.Builder(this, CHANNEL_ID2)
                .setContentTitle("Timer is running")
                .setContentText(updateNotification(time))
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .setSound(null)
                .build()
            startForeground(2, notification)
        } else { // khi het thoi gian thi se tao 1 thong bao khac, co chuong keu

            mediaPlayer.start()

            val remoteViews = RemoteViews(packageName, R.layout.custom_noti)
            val closeIntent = Intent(this, TimerService::class.java)
            closeIntent.action = STOP
            val pIntent =
                PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.btnStopTimer, pIntent)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID2)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build()
            startForeground(2, notification)
        }
    }

    private fun updateNotification(time: Long): String {  // update UI cho noti
        val hour = time / 3600
        val minute = time / 60 % 60
        val second = time % 60
        val sharePreferences = getSharedPreferences("pref", MODE_PRIVATE)
        sharePreferences.edit().putLong("time left", time).apply()
        return (String.format("%02d", hour) + ":" + String.format(
            "%02d",
            minute
        ) + ":" + String.format("%02d", second))

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        countDownTimer.cancel()

    }
}