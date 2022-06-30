package viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import model.AlarmRepository
import javax.inject.Inject

@HiltViewModel
class AlarmRingViewModel @Inject constructor(private val repository: AlarmRepository): ViewModel() {

    private fun update(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(alarm)
        }
    }

//    fun updateAlarmSwitch(id: Long) {
//        val alarm = repository.getAlarmWithId(id)
//        alarm.isOn = false
//        update(alarm)
//    }
}