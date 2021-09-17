package service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder

class TimerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    var countDownTimer: CountDownTimer? = null
    override fun onCreate() {
        super.onCreate()

    }
}