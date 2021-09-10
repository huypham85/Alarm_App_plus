package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.databinding.AlarmItemBinding
import model.Alarm

class AlarmListAdapter(val onToggle:(Alarm)->Unit): RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {
    lateinit var listAlarm: MutableList<Alarm>
    inner class ViewHolder(private val binding:AlarmItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm){
            with(binding){
                itemAlarmTime.text = "${alarm.hour}:${alarm.minute}"
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AlarmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.bind(alarm)
        holder.setOnToggle(listAlarm[position])
    }

    override fun getItemCount(): Int {
        return listAlarm.size
    }

}