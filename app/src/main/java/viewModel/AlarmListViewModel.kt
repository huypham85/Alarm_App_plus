package viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmDatabase
import model.AlarmRepository
import java.util.concurrent.ThreadLocalRandom

class AlarmListViewModel(application: Application):ViewModel() {
    private var alarmRepository: AlarmRepository
    var listAlarmLiveData : LiveData<List<Alarm>> = MutableLiveData()

    init {
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
        alarmRepository = AlarmRepository(alarmDao)
        listAlarmLiveData = alarmRepository.getAlarms
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
            isOn = false,
            System.currentTimeMillis(),
        )
        insert(alarm = newAlarm)
    }

//    companion object{
//        var index =3
//        private var INSTANCE: AlarmListViewModel? = null
//        fun getInstance(repository: AlarmRepository): AlarmListViewModel {
//            if(INSTANCE == null){
//                INSTANCE = AlarmListViewModel(repository)
//            }
//            return INSTANCE!!
//        }
//    }
    fun insert(alarm: Alarm){
        Log.e("View model", "insert")
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.insert(alarm)
        }
        Log.e("list after insert", "${listAlarmLiveData.value?.size}")
//        Log.e("list size", listAlarm.size.toString())
//        Log.e("list db size", alarmRepository.liveDataAlarms.value?.size.toString())
//        Log.e("id viewModel",alarm.id.toString())
//        Log.e("live list size", _liveListAlarm.value?.size.toString())
    }
    fun update(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.update(alarm)
        }

    }
}