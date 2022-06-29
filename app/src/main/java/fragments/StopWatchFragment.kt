package fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentStopWatchBinding
import service.StopwatchService
import kotlin.math.roundToInt

class StopWatchFragment : Fragment() {
    private lateinit var binding: FragmentStopWatchBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStopWatchBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceIntent = Intent(context, StopwatchService::class.java)

        val sharedPreferences = activity?.getSharedPreferences("stopWatchPref", MODE_PRIVATE)
        val timeRunning = sharedPreferences?.getFloat("timeRunning", 0.0F)
        val lastStarted = sharedPreferences?.getBoolean("started", false)
        if (timeRunning != null && lastStarted == true) { // truoc khi tat app: time van chay va ko bi stop
            if (timeRunning > 0) {
                binding.txtTime.text = getTimeStringFromDouble(timeRunning.toDouble())
                LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                    broadcastReceiver,
                    IntentFilter(StopwatchService.TIMER_UPDATED)
                )
                binding.btnStart.text = "Stop"
                binding.btnStart.background = resources.getDrawable(R.drawable.stop_round_button)
                timerStarted = true
                binding.btnReset.isEnabled = false
            } else {
                time = 0.0
                binding.txtTime.text = getTimeStringFromDouble(time)
            }

        }
        binding.btnStart.setOnClickListener {
            startStopTimer()
        }
        binding.btnReset.setOnClickListener {
            resetTimer()
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(broadcastReceiver, IntentFilter(StopwatchService.TIMER_UPDATED))
    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(StopwatchService.TIME_EXTRA, 0.0)
            binding.txtTime.text = getTimeStringFromDouble(time)
        }
    }

    private fun resetTimer() {
        //Log.e("stopwatch", "reset")
        stopTimer()
        time = 0.0
        binding.txtTime.text = getTimeStringFromDouble(time)
        val sharedPreferences = this.activity?.getSharedPreferences("stopWatchPref", MODE_PRIVATE)
        with(sharedPreferences?.edit()) {
            this?.putFloat("timeRunning", 0.0F)
            this?.apply()
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val millis = resultInt % 100
        val seconds = resultInt / 100 % 60
        val minutes = resultInt / 100 / 60 % 60

        return makeTimeString(minutes, seconds, millis)
    }

    private fun makeTimeString(minutes: Int, seconds: Int, millis: Int): String {
        return String.format("%02d:%02d:%02d", minutes, seconds, millis)
    }

    private fun startStopTimer() {
        if (timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        val sharedPreferences = activity?.getSharedPreferences("stopWatchPref", MODE_PRIVATE)
        with(sharedPreferences?.edit()) {
            this?.putBoolean("started", true)
            this?.apply()
        }
        serviceIntent.putExtra(StopwatchService.TIME_EXTRA, time)
        context?.startService(serviceIntent)
        binding.btnStart.text = "Stop"
        binding.btnStart.background = resources.getDrawable(R.drawable.stop_round_button)
        timerStarted = true
        binding.btnReset.isEnabled = false
    }

    private fun stopTimer() {
        val sharedPreferences = activity?.getSharedPreferences("stopWatchPref", MODE_PRIVATE)
        with(sharedPreferences?.edit()) {
            this?.putBoolean("started", false)
            this?.apply()
        }
        context?.stopService(serviceIntent)
        binding.btnStart.text = "Start"
        binding.btnStart.background = resources.getDrawable(R.drawable.start_round_button)
        timerStarted = false
        binding.btnReset.isEnabled = true
    }

}