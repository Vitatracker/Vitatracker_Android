package app.mybad.notifier.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.usecases.meds.LoadMedsFromListUseCase
import app.mybad.domain.usecases.usages.LoadUsagesAllUseCase
import app.mybad.domain.usecases.usages.LoadUsagesByIntervalUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.notifier.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StartMainScreenViewModel @Inject constructor(
    private val loadUsagesByIntervalUseCase: LoadUsagesByIntervalUseCase,
    private val loadUsagesAllUseCase: LoadUsagesAllUseCase,
    private val loadMedsFromList:  LoadMedsFromListUseCase,
    private val updateUsageUseCase: UpdateUsageUseCase,
    private val coursesNetworkRepo: CoursesNetworkRepo,
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
            updateUsageUseCase.execute(usage = usage)
            coursesNetworkRepo.updateUsage(usage)
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
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(date = LocalDateTime.now())) }
    }

    private fun getAllUsages() {
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(allUsages = loadUsagesAllUseCase().size))
        }
    }

    private fun updateUsages() {
        viewModelScope.launch {
            _uiState.emit(
                uiState.value.copy(
                    usages = loadUsagesByIntervalUseCase.execute(
                        uiState.value.date
                            .withHour(0).withMinute(0).withSecond(0)
                            .toLong(),
                        uiState.value.date
                            .withHour(23).withMinute(59).withSecond(59)
                            .toLong()
                    )
                )
            )
            updateMeds()
        }
    }

    private fun updateMeds() {
        val listMeds: List<Long> = uiState.value.usages.map { it.medId }.toSet().toList()

        viewModelScope.launch {
            _uiState.emit(
                uiState.value.copy(
                    meds = loadMedsFromList.execute(listMedsId = listMeds)
                )
            )
        }
    }

}
