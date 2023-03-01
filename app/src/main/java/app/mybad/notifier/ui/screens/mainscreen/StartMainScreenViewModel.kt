package app.mybad.notifier.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.notifier.ui.screens.mainscreen.MainScreenContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StartMainScreenViewModel @Inject constructor(
    private val courses: CoursesRepo,
    private val usages: UsagesRepo,
    private val meds: MedsRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(MainScreenContract())
    var uiState = _uiState.asStateFlow()

    init {
        scope.launch {
            courses.getAllFlow().collect { _uiState.emit(_uiState.value.copy(courses = it)) }
        }
        scope.launch {
            meds.getAllFlow().collect { _uiState.emit(_uiState.value.copy(meds = it)) }
        }
        scope.launch {
            usages.getCommonAllFlow().collect { _uiState.emit(_uiState.value.copy(usages = it)) }
        }
    }

    fun changeData(date: LocalDate) {
        _uiState.value.date = date
        Log.d("MainScreen", "date: $date")
    }

}