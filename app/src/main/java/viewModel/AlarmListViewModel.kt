package viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmDatabase
import model.AlarmRepository

class AlarmListViewModel(application: Application):ViewModel() {

    private val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
    private var alarmRepository: AlarmRepository = AlarmRepository(alarmDao)
//    var listAlarmLiveData : LiveData<List<Alarm>> = liveData(IO) {
//        //delay(3000)
////        alarmRepository.getAlarms.value?.let { emit(it) }
//        emit(alarmRepository.getAlarms())
//    }
    private val listAlarms = MutableLiveData<List<Alarm>>()
    var listAlarmLiveData : LiveData<List<Alarm>> = listAlarms

    init {
        viewModelScope.launch(Dispatchers.IO) {
            listAlarms.postValue(alarmRepository.getAlarms())
        }
    }

    suspend fun insert(alarm: Alarm){
        Log.e("View model", "insert")
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.insert(alarm)
        }.join()
        //Log.e("list after insert", "${listAlarmLiveData.value?.size}")
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

    fun delete(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.delete(alarm)
            //listAlarmLiveData.value  = alarmRepository.getAlarms()
        }
    }
}