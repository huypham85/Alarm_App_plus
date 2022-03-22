package viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmDatabase
import model.AlarmRepository

class AlarmListViewModel(application: Application) : ViewModel() {

    private val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
    private var alarmRepository: AlarmRepository = AlarmRepository(alarmDao)
    private val listAlarms = MutableLiveData<List<Alarm>>()
    var listAlarmLiveData: LiveData<List<Alarm>> = listAlarms

    init {
        viewModelScope.launch(Dispatchers.IO) {
            listAlarms.postValue(alarmRepository.getAlarms())
        }
    }

    suspend fun insert(alarm: Alarm) {
        Log.e("View model", "insert")
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.insert(alarm)
        }.join()
    }

    fun update(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.update(alarm)
//            listAlarms.postValue(alarmRepository.getAlarms())
        }
    }

    fun delete(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.delete(alarm)
            listAlarms.postValue(alarmRepository.getAlarms())
        }
    }
}