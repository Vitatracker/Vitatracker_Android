package app.mybad.notifier.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.usecases.meds.LoadMedsFromListUseCase
import app.mybad.domain.usecases.usages.LoadUsagesAllUseCase
import app.mybad.domain.usecases.usages.LoadUsagesByIntervalUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.notifier.utils.atEndOfDayInEpochSeconds
import app.mybad.notifier.utils.atStartOfDayInEpochSeconds
import app.mybad.notifier.utils.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StartMainScreenViewModel @Inject constructor(
    private val loadUsagesByInterval: LoadUsagesByIntervalUseCase,
    private val loadUsagesAll: LoadUsagesAllUseCase,
    private val loadMedsFromList:  LoadMedsFromListUseCase,
    private val updateUsage: UpdateUsageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenContract())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setDataNow()
            updateUsages()
            getAllUsages()
        }
    }

    fun setUsagesFactTime(usage: UsageCommonDomainModel) {
        viewModelScope.launch {
            updateUsage.execute(usage = usage)
//                medId = usage.medId,
//                usageTime = usage.useTime,
//                factTime = convertDateToLong(LocalDateTime.now())
//            )
            updateUsages()
        }
    }

    fun changeData(date: LocalDateTime) {
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(date = date)) }
        Log.d("MainScreen", "changeData: $date")
        updateUsages()
    }

    private fun setDataNow() {
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(date = getCurrentDateTime())) }
    }

    private fun getAllUsages() {
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(allUsages = loadUsagesAll().size))
        }
    }

    private fun updateUsages() {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    usages = loadUsagesByInterval(
                        _uiState.value.date.atStartOfDayInEpochSeconds(),
                        _uiState.value.date.atEndOfDayInEpochSeconds(),
                    )
                )
            )
            updateMeds()
        }
    }

    private fun updateMeds() {
        viewModelScope.launch {
            val listMeds: List<Long> = _uiState.value.usages.map { it.medId }.toSet().toList()
            _uiState.emit(
                _uiState.value.copy(
                    meds = loadMedsFromList.execute(listMedsId = listMeds)
                )
            )
        }
    }

}
