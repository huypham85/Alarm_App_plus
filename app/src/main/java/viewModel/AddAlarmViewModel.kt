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
class AddAlarmViewModel @Inject constructor(private val repository: AlarmRepository) : ViewModel() {
    suspend fun insert(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(alarm)
        }.join()
    }
}