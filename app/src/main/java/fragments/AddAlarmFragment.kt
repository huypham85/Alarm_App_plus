package fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import model.Alarm
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentAddAlarmBinding
import viewModel.AlarmListViewModel
import java.util.concurrent.ThreadLocalRandom

class AddAlarmFragment : Fragment() {
    private lateinit var binding: FragmentAddAlarmBinding
    private lateinit var alarmListViewModel: AlarmListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddAlarmBinding.inflate(inflater,container,false)
        alarmListViewModel = ViewModelProvider(requireActivity()).get(AlarmListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtSave.setOnClickListener { //tao 1 bao thuc moi
            createAlarm()
            Navigation.findNavController(view).navigate(R.id.action_addAlarmFragment_to_alarmFragment)
        }
        binding.checkRecur.setOnCheckedChangeListener{ _: CompoundButton, isChecked: Boolean ->
            if(isChecked){
                binding.recurLayout.visibility = View.VISIBLE
            }
            else{
                binding.recurLayout.visibility = View.GONE
            }
        }
        binding.txtCancel.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_addAlarmFragment_to_alarmFragment)
        }
    }
    private fun createAlarm(){
        var alarm = Alarm(binding.timePicker.hour,
                binding.timePicker.minute,
                binding.checkRecur.isChecked,
                binding.checkMon.isChecked,
                binding.checkTue.isChecked,
                binding.checkWed.isChecked,
                binding.checkThu.isChecked,
                binding.checkFri.isChecked,
                binding.checkSat.isChecked,
                binding.checkSun.isChecked,
                binding.edtAlarmLabel.text.toString(),
                ThreadLocalRandom.current().nextInt(),
                true
        )
        alarmListViewModel.addAlarm(alarm)
        Log.e("Random ID",alarm.id.toString())
        val hour: Int = binding.timePicker.hour
        val minute: Int = binding.timePicker.minute
        Toast.makeText(requireActivity(), "$hour : $minute", Toast.LENGTH_LONG).show()
        context?.let { alarm.schedule(it) }
    }
}