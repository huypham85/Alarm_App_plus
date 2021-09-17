package fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentTimerBinding
import service.TimerService
import viewModel.TimerViewModel

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    lateinit var timerViewModel: TimerViewModel
    private var isActive = false
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timerToolbar.inflateMenu(R.menu.timer_menu)
        binding.timePicker.setIs24HourView(true)
        binding.timePicker.hour = 0
        binding.timePicker.minute = 0

//        val intent = Intent(context,TimerService::class.java)
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            context?.startForegroundService(intent)
//        }
//        else{
//            context?.startService(intent)
//        }

        timerViewModel = ViewModelProvider(requireActivity()).get(TimerViewModel::class.java)
        timerViewModel.timeLeft.observe(viewLifecycleOwner, {
            binding.txtTime.text = it
            binding.progressBar.progress = (timerViewModel.timerLength - timerViewModel.time).toInt() // set the progressbar
            Log.e("progress", "${binding.progressBar.progress}")
        })
        timerViewModel.timeOut.observe(viewLifecycleOwner, {
            if (it){
                resetTimer()
                isActive = false
            }
        })
//        if(timerViewModel.timeOut.value == true){
//            resetTimer()
//            binding.progressBar.progress = 0
//        }

        binding.btnStart.setOnClickListener {
            isActive = true
            binding.btnStart.isEnabled = false
            binding.btnPause.isEnabled = true
            binding.btnStop.isEnabled = true
            binding.timePicker.visibility = View.GONE
            binding.timeLeftLayout.visibility = View.VISIBLE
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            binding.progressBar.max = timerViewModel.setTimeProgress(0,minute,hour).toInt()
            timerViewModel.setNewTime(0,minute,hour)
            timerViewModel.startTimer()
        }
        binding.btnPause.setOnClickListener {
            if(isActive){
                timerViewModel.finishTimer()
                binding.btnPause.text = "Resume"
                isActive = false
            }
            else{
                timerViewModel.startTimer()
                isActive = true
                binding.btnPause.text = "Pause"
            }
        }
        binding.btnStop.setOnClickListener {
            isActive = false
            resetTimer()
            timerViewModel.finishTimer()
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
}