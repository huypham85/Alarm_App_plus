package viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private lateinit var countDownTimer:CountDownTimer
    var timeLeft = MutableLiveData<String>() // time to observe
    var timeOut = MutableLiveData<Boolean>()
    var time = 0L // time set to countdown
    var timerLength =0L

    fun startTimer(){
        timeOut.postValue(false)
        countDownTimer = object : CountDownTimer(time*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                time = millisUntilFinished/1000  // use to restart timer when click resume
                updateUI()
            }

            override fun onFinish() {
                Log.e("Cd timer", "On finish")
                timeOut.postValue(true)
                finishTimer()
            }

        }.start()
    }

    fun setTimeProgress(sec:Int, min:Int,hour:Int) : Long{
        return (hour*3600 + min*60 + sec).toLong()
    }

    fun setNewTime(sec:Int, min:Int,hour:Int){
        time = (hour*3600 + min*60 + sec).toLong()
        timerLength = time
    }

    private fun updateUI() {
        Log.e("Update UI Timer", "On")
        val hour = time/3600
        val minute = time/60%60
        val second = time%60
        timeLeft.postValue(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second))
    }

    fun finishTimer(){
        Log.e("Finish Timer", "On")
        countDownTimer.cancel()
    }

}