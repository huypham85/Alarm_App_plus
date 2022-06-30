package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmRepository
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(private val alarmRepository: AlarmRepository) :
    ViewModel() {
    private var _listAlarms = MutableLiveData<List<Alarm>>()
    var listAlarms: LiveData<List<Alarm>> = _listAlarms

    init {
       getData()
    }

    fun getData(){
        viewModelScope.launch(Dispatchers.IO) {
            _listAlarms.postValue(alarmRepository.getAlarms())
        }
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
            _listAlarms.postValue(alarmRepository.getAlarms())
        }
    }
}