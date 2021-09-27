package adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.alarmapp.databinding.AlarmItemBinding
import model.Alarm
import viewModel.AlarmListViewModel

class AlarmListAdapter(val onToggle:(Alarm)->Unit, var viewModel: AlarmListViewModel, var context:Context): RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder>() {
    var listAlarm: List<Alarm> = mutableListOf()
    private var viewBinderHelper = ViewBinderHelper()

    inner class AlarmViewHolder(private var binding: AlarmItemBinding): RecyclerView.ViewHolder(binding.root) {
        val itemLayout = binding.itemLayout
        val layoutDelete = binding.layoutDelete
        @SuppressLint("SetTextI18n")
        fun bind(alarm: Alarm){
            with(binding){
                if(alarm.minute < 10) itemAlarmTime.text = "${alarm.hour}:${"0"+ alarm.minute}"
                else itemAlarmTime.text = "${alarm.hour}:${alarm.minute}"
                if(alarm.label.isNotEmpty()) itemAlarmLabel.text = alarm.label
                else itemAlarmLabel.text = "Alarm"
                itemAlarmRecurringDays.text = alarm.getRecurringDays()

                Log.e("Alarm status", alarm.isOn.toString())
                binding.itemAlarmSwitch.isChecked = alarm.isOn

                binding.itemAlarmSwitch.setOnCheckedChangeListener { _: CompoundButton, bool: Boolean ->
                    onToggle(alarm)
                    //alarm.isOn = bool
                    Log.e("Alarm status after switch", alarm.isOn.toString())
                }
            }
        }
        fun setOnToggle(alarm: Alarm) {
//            binding.itemAlarmSwitch.setOnCheckedChangeListener { _: CompoundButton, bool: Boolean ->
//                alarm.isOn = bool
//                onToggle(alarm)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        var binding =AlarmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmListAdapter.AlarmViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.apply {
            bind(alarm)
            setOnToggle(alarm)
        }
        viewBinderHelper.bind(holder.itemLayout, alarm.id.toString())
        holder.layoutDelete.setOnClickListener {
            alarm.cancelAlarm(context)
            viewModel.delete(alarm)
        }
    }

    override fun getItemCount(): Int {
        return listAlarm.size
    }

}