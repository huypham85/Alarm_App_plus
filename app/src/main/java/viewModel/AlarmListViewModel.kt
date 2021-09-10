package viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Alarm
import java.util.concurrent.ThreadLocalRandom

class AlarmListViewModel:ViewModel() {
    var liveListAlarm: MutableLiveData<MutableList<Alarm>> = MutableLiveData()
    private var listAlarm: MutableList<Alarm> = ArrayList()

    init {
        val newAlarm = Alarm(
            9,
            30,
            recurring = false,
            monday = false,
            tuesday = false,
            wednesday = false,
            thursday = false,
            friday = false,
            saturday = false,
            sunday = false,
            label = "Alarm",
            id = ThreadLocalRandom.current().nextInt(),
            isOn = false
        )
        listAlarm.add(newAlarm)
        liveListAlarm.value = listAlarm
    }
    fun addAlarm(alarm: Alarm){
        listAlarm.add(alarm)
        liveListAlarm.value = listAlarm
    }
}