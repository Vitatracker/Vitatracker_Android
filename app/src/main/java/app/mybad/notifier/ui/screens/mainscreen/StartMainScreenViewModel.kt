package app.mybad.notifier.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.domain.usecases.remedies.GetRemediesByListIdUseCase
import app.mybad.domain.usecases.usages.GetUsagesBetweenUseCase
import app.mybad.domain.usecases.usages.GetUsagesUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTime
import app.mybad.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StartMainScreenViewModel @Inject constructor(
    private val getRemediesByListIdUseCase: GetRemediesByListIdUseCase,
    private val getCoursesUseCase: GetCoursesUseCase,
    private val getUsagesBetweenUseCase: GetUsagesBetweenUseCase,
    private val getUsagesUseCase: GetUsagesUseCase,
    private val updateUsage: UpdateUsageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenContract())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setDataNow()
            updateUsages()
//            getUsages()
        }
    }

    fun setUsagesFactTime(usage: UsageDomainModel) {
        viewModelScope.launch {
            updateUsage(usage)
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
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(date = currentDateTime())) }
    }

    private fun getUsages() {
        viewModelScope.launch {
            getUsagesUseCase().onSuccess {usages->
                _uiState.emit(
                    _uiState.value.copy(
                        usages = usages,
                        usagesSize = usages.size,
                    ))
            }
                .onFailure {
                    TODO("вывод ошибки")
                }
        }
    }

    private fun updateUsages() {
        viewModelScope.launch {
            getUsagesBetweenUseCase(
                _uiState.value.date.atStartOfDay().toEpochSecond(),
                _uiState.value.date.atEndOfDay().toEpochSecond(),
            ).onSuccess { usages ->
                _uiState.emit(_uiState.value.copy(
                    usages = usages,
                    usagesSize = usages.size
                ))
                updateRemedy()
            }
                .onFailure {
                    TODO("вывод ошибки")
                }
        }
    }

    private fun updateRemedy() {
        viewModelScope.launch {
            getCoursesUseCase().onSuccess {courses->
                _uiState.emit(_uiState.value.copy(courses = courses))
            }
                .onFailure {
                    TODO("вывод ошибки")
                }
            val remedyIds = _uiState.value.usages.map {usage-> usage.remedyId }.toList()
//            val coursesIds = _uiState.value.usages.map {usage-> usage.courseId }.toSet()
//            val remedyIds = _uiState.value.courses.filter { course->
//                course.id in coursesIds
//            }.map {course->
//                course.remedyId
//            }.toSet().toList()
            getRemediesByListIdUseCase(remedyIds).onSuccess { remedies->
                _uiState.emit(_uiState.value.copy(remedies = remedies))
            }
                .onFailure {
                    TODO("вывод ошибки")
                }
        }
    }

}
