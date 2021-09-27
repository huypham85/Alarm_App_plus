package fragments

import android.app.Service
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentTimerBinding
import service.TimerService

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    //lateinit var timerViewModel: TimerViewModel
    private var isActive:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimerBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timerToolbar.inflateMenu(R.menu.timer_menu)
        binding.timePicker.setIs24HourView(true)

//        val filter = IntentFilter(TimerService.COUNTDOWN)
//        fil

        var sharedPreferences = this.activity?.getSharedPreferences("pref",MODE_PRIVATE)
        var timeLeft = sharedPreferences?.getLong("time left",0)
        if (timeLeft != null) {
            if(timeLeft >= 1) {
                isActive = true
                binding.btnStart.isEnabled = false
                binding.btnPause.isEnabled = true
                binding.btnStop.isEnabled = true
                binding.timePicker.visibility = View.GONE
                binding.timeLeftLayout.visibility = View.VISIBLE
                val hour = timeLeft/3600
                val minute = timeLeft/60%60
                val second = timeLeft%60
                binding.txtTime.text = (String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second))
                LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broadcastReceiver, IntentFilter(TimerService.COUNTDOWN))
            }
            else{
                Log.e("Reset timer","true")
                resetTimer()
            }
        }



        // view model
        //timerViewModel = ViewModelProvider(requireActivity()).get(TimerViewModel::class.java)
//        timerService.timeLeft.observe(viewLifecycleOwner, {
//            binding.txtTime.text = it
//            binding.progressBar.progress = (timerService.timerLength - timerService.time).toInt() // set the progressbar
//            Log.e("progress", "${binding.progressBar.progress}")
//        })
//        timerService.timeOut.observe(viewLifecycleOwner, {
//            if (it){
//                resetTimer()
//                isActive = false
//            }
//        })

        binding.btnStart.setOnClickListener {
            isActive = true
            binding.btnStart.isEnabled = false
            binding.btnPause.isEnabled = true
            binding.btnStop.isEnabled = true
            binding.timePicker.visibility = View.GONE
            binding.timeLeftLayout.visibility = View.VISIBLE
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            binding.progressBar.max = setNewTime(0,minute,hour).toInt()
//            timerService.setNewTime(0,minute,hour)

            //timerService.startTimer()
            val intent = Intent(context, TimerService::class.java)
            intent.putExtra("New time", setNewTime(0,minute,hour))
            context?.startService(intent)
        }
//        binding.btnPause.setOnClickListener {
//            if(isActive){
//                timerService.finishTimer()
//                binding.btnPause.text = "Resume"
//                isActive = false
//            }
//            else{
//                timerService.startTimer()
//                isActive = true
//                binding.btnPause.text = "Pause"
//            }
//        }
//        binding.btnStop.setOnClickListener {
//            isActive = false
//            resetTimer()
//            timerService.finishTimer()
//        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broadcastReceiver, IntentFilter(TimerService.COUNTDOWN))
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broadcastReceiver, IntentFilter(TimerService.TIME_OUT))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broadcastReceiver)
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()

    }
    private var broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            //intent?.let { updateUI(it) }
            if (intent != null) {
                if(TimerService.COUNTDOWN == intent.action){
                    updateUI(intent)
                }
                if(TimerService.TIME_OUT == intent.action){
                    resetTimer()
                }
            }
        }

    }

    private fun resetTimer() {
        binding.timePicker.hour = 0
        binding.timePicker.minute = 0
        binding.btnPause.isEnabled = false
        binding.btnStop.isEnabled = false
        binding.btnStart.isEnabled = true
        binding.btnPause.text = "Pause"
        binding.timePicker.visibility = View.VISIBLE
        binding.timeLeftLayout.visibility = View.GONE
    }
    private fun setNewTime(sec: Int, min: Int, hour: Int): Long {
        return (hour * 3600 + min * 60 + sec).toLong()
        //timerLength = time
    }
    private fun updateUI(intent: Intent) {
        var time = intent.getLongExtra("countdown", 0)
        val hour = time / 3600
        val minute = time / 60 % 60
        val second = time % 60
        binding.txtTime.text = (String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second))
        binding.progressBar.progress = binding.progressBar.max - time.toInt()
    }
}