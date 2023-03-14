package app.mybad.notifier.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
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
    private val usages: UsagesRepo,
    private val meds: MedsRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(MainScreenContract())
    var uiState = _uiState.asStateFlow()

    init {
        scope.launch {
            _uiState.emit(_uiState.value.copy(date = LocalDateTime.now()))
            updateUsages()
        }
    }

    fun changeData(date: LocalDateTime) {
        scope.launch { _uiState.emit(_uiState.value.copy(date = date)) }
        Log.d("MainScreen", "changeData: $date")
        updateUsages()
    }

    private fun updateUsages() {
        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    usages = usages.getUsagesByInterval(
                        convertDateToLong(
                            _uiState.value.date.withHour(0).withMinute(0).withSecond(0)
                        ),
                        convertDateToLong(
                            _uiState.value.date.withHour(23).withMinute(59).withSecond(59)
                        )
                    )
                )
            )
            Log.d("MainScreen", "usages: ${_uiState.value.usages.size}")
            updateMeds()
        }
    }

    private fun updateMeds() {
        val listMeds: List<Long> = _uiState.value.usages.map { it.medId }.toSet().toList()

        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    meds = meds.getFromList(listMedsId = listMeds)
                )
            )
            Log.d("MainScreen", "meds: ${_uiState.value.meds.size}")
        }
    }

    private fun convertDateToLong(date: LocalDateTime): Long {
        val zdt: ZonedDateTime = ZonedDateTime.of(date, ZoneId.systemDefault())
        return zdt.toInstant().toEpochMilli() / 1000
    }
}