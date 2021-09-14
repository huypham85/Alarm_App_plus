package viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import model.Alarm
import model.AlarmRepository
import java.util.concurrent.ThreadLocalRandom

class AlarmListViewModel(private val alarmRepository: AlarmRepository):ViewModel() {
    var liveListAlarm: MutableLiveData<MutableList<Alarm>> = MutableLiveData<MutableList<Alarm>>()
    var listAlarm = mutableListOf<Alarm>()
    init {
        alarmRepository.liveDataAlarms.value?.let { listAlarm.addAll(it) }
        Log.e("live list size init", alarmRepository.liveDataAlarms.value?.size.toString())
        liveListAlarm.postValue(listAlarm)
        Log.e("list size init", listAlarm.size.toString())
//        val newAlarm = Alarm(
//            9,
//            30,
//            recurring = false,
//            monday = false,
//            tuesday = false,
//            wednesday = false,
//            thursday = false,
//            friday = false,
//            saturday = false,
//            sunday = false,
//            label = "Alarm",
//            isOn = false,
//            System.currentTimeMillis(),
//                index++
//        )
//        insert(alarm = newAlarm)
    }

    companion object{
        var index =3
        private var INSTANCE: AlarmListViewModel? = null
        fun getInstance(repository: AlarmRepository): AlarmListViewModel {
            if(INSTANCE == null){
                INSTANCE = AlarmListViewModel(repository)
            }
            return INSTANCE!!
        }
    }
    fun insert(alarm: Alarm){
        Log.e("View model", "insert")
        alarmRepository.insert(alarm)
        listAlarm.add(alarm)
        liveListAlarm.postValue(listAlarm)
        Log.e("list size", listAlarm.size.toString())
        Log.e("list db size", alarmRepository.liveDataAlarms.value?.size.toString())
        Log.e("id viewModel",alarm.id.toString())
        Log.e("live list size", liveListAlarm.value?.size.toString())
    }
    fun update(alarm: Alarm){
        alarmRepository.update(alarm)
        liveListAlarm.value?.add(alarm)
    }
}