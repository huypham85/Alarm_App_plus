package service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapp.R
import fragments.TimerFragment
import notifications.AppNoti
import notifications.AppNoti.Companion.CHANNEL_ID

class TimerService : Service() {

    private lateinit var countDownTimer:CountDownTimer
    var intentService = Intent(COUNTDOWN)


    companion object{
        const val COUNTDOWN = "backgroundtimer"
        const val TIME_OUT = "time_out"
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e("Timer service", "On start command")
        startTimer(intent)

        return START_STICKY
    }

    fun startTimer(intent: Intent?){
        var time = intent?.getLongExtra("New time",0)
        countDownTimer = object : CountDownTimer(time?.times(1000)!!,1000){
            override fun onTick(millisUntilFinished: Long) {
                intentService.putExtra("countdown",millisUntilFinished/1000)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentService)
                sendNotification(millisUntilFinished/1000)
            }

            override fun onFinish() {
                val intentTimeOut = Intent(TIME_OUT)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentTimeOut)
                stopSelf()
            }

        }.start()
    }

    private fun sendNotification(time: Long){
        val intent = Intent(this, TimerFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer is running")
            .setContentText(updateNotification(time))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1,notification)
    }

    private fun updateNotification(time : Long) : String{
        Log.e("Update UI Timer", "On")
        val hour = time/3600
        val minute = time/60%60
        val second = time%60
        val sharePreferences = getSharedPreferences("pref", MODE_PRIVATE)
        sharePreferences.edit().putLong("time left",time).apply()
        return (String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second))

    }
//
//    fun finishTimer(){
//        Log.e("Finish Timer", "On")
//        countDownTimer.cancel()
//    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }
}