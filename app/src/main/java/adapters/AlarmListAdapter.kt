package adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.databinding.AlarmItemBinding
import model.Alarm
import viewModel.AlarmListViewModel

class AlarmListAdapter(val onToggle:(Alarm)->Unit, var viewModel: AlarmListViewModel): RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder>() {
    var listAlarm: List<Alarm> = mutableListOf()
    private lateinit var binding:AlarmItemBinding

    inner class AlarmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(alarm: Alarm){
            with(binding){
                if(alarm.minute < 10) itemAlarmTime.text = "${alarm.hour}:${"0"+ alarm.minute}"
                else itemAlarmTime.text = "${alarm.hour}:${alarm.minute}"
                if(alarm.label.isNotEmpty()) itemAlarmLabel.text = alarm.label
                else itemAlarmLabel.text = "Alarm"
                itemAlarmRecurringDays.text = alarm.getRecurringDays()
                itemAlarmSwitch.isChecked = alarm.isOn
            }
        }
        fun setOnToggle(alarm: Alarm){
            binding.itemAlarmSwitch.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
                onToggle(alarm)
            }
        }
        fun setOnEdit(alarm: Alarm){
            if (binding.btnDeleteAlarm.visibility == View.VISIBLE){
                binding.btnDeleteAlarm.setOnClickListener {
                    Log.d("item", "delete")
                }
                binding.root.setOnClickListener{
                    Log.d("item", "onClick")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        binding =AlarmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlarmViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AlarmListAdapter.AlarmViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.apply {
            bind(alarm)
            setOnEdit(alarm)
            setOnToggle(alarm)
        }
    }

    override fun getItemCount(): Int {
        return listAlarm.size
    }

}