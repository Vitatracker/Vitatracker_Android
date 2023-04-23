package app.mybad.notifier.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.usecases.meds.LoadMedsFromList
import app.mybad.domain.usecases.usages.LoadUsagesAllUseCase
import app.mybad.domain.usecases.usages.LoadUsagesByIntervalUseCase
import app.mybad.domain.usecases.usages.UpdateUsageFactTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class StartMainScreenViewModel @Inject constructor(
    private val loadUsagesByIntervalUseCase: LoadUsagesByIntervalUseCase,
    private val loadUsagesAllUseCase: LoadUsagesAllUseCase,
    private val loadMedsFromList: LoadMedsFromList,
    private val updateUsageFactTimeUseCase: UpdateUsageFactTimeUseCase
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(MainScreenContract())
    var uiState = _uiState.asStateFlow()

    init {
        scope.launch {
            setDataNow()
            updateUsages()
            getAllUsages()
        }
    }

    fun setUsagesFactTime(medId: Long, usageTime: Long, factTime: Long) {
        scope.launch {
            updateUsageFactTimeUseCase.execute(
                medId = medId,
                usageTime = usageTime,
                factTime = factTime
            )
            updateUsages()
        }
    }

    fun changeData(date: LocalDateTime) {
        scope.launch { _uiState.emit(_uiState.value.copy(date = date)) }
        Log.d("MainScreen", "changeData: $date")
        updateUsages()
    }

    private fun setDataNow() {
        scope.launch { _uiState.emit(_uiState.value.copy(date = LocalDateTime.now())) }
    }

    private fun getAllUsages() {
        scope.launch { _uiState.emit(_uiState.value.copy(allUsages = loadUsagesAllUseCase.execute().size)) }
    }

    private fun updateUsages() {
        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    usages = loadUsagesByIntervalUseCase.execute(
                        convertDateToLong(
                            _uiState.value.date.withHour(0).withMinute(0).withSecond(0)
                        ),
                        convertDateToLong(
                            _uiState.value.date.withHour(23).withMinute(59).withSecond(59)
                        )
                    )
                )
            )
            updateMeds()
        }
    }

    private fun updateMeds() {
        val listMeds: List<Long> = _uiState.value.usages.map { it.medId }.toSet().toList()

        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    meds = loadMedsFromList.execute(listMedsId = listMeds)
                )
            )
        }
    }

    private fun convertDateToLong(date: LocalDateTime): Long {
        val zdt: ZonedDateTime = ZonedDateTime.of(date, ZoneId.systemDefault())
        return zdt.toInstant().toEpochMilli() / 1000
    }
}