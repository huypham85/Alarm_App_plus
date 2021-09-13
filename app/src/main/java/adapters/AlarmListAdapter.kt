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

class AlarmListAdapter(val onToggle:(Alarm)->Unit): RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {
    var listAlarm: List<Alarm> = mutableListOf()

    inner class ViewHolder(private val binding:AlarmItemBinding): RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AlarmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.bind(alarm)
        holder.setOnToggle(listAlarm[position])
        holder.setOnEdit(listAlarm[position])
    }

    override fun getItemCount(): Int {
        return listAlarm.size
    }

}