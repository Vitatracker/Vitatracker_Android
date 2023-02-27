package app.mybad.notifier

import androidx.lifecycle.ViewModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

@HiltViewModel
class StartMainScreenViewModel : ViewModel() {

    private val coursesList = listOf(
        CourseDomainModel(id = 1L, medId = 1L, startDate = 0L, endDate = 11000000L),
        CourseDomainModel(id = 2L, medId = 2L, startDate = 0L, endDate = 12000000L),
        CourseDomainModel(id = 3L, medId = 3L, startDate = 0L, endDate = 13000000L),
    )

    private val medsList = listOf(
        MedDomainModel(
            id = 1L,
            name = "Doliprane",
            details = MedDetailsDomainModel(
                type = 1,
                dose = 500,
                measureUnit = 1,
                icon = R.drawable.pill
            )
        ),
        MedDomainModel(
            id = 2L,
            name = "Dexedrine",
            details = MedDetailsDomainModel(
                type = 1,
                dose = 30,
                measureUnit = 1,
                icon = R.drawable.pill
            )
        ),
        MedDomainModel(
            id = 3L,
            name = "Prozac",
            details = MedDetailsDomainModel(
                type = 1,
                dose = 120,
                measureUnit = 1,
                icon = R.drawable.pill
            )
        ),
    )

    private val _uiState = MutableStateFlow(LocalDate.now())
    var uiState = _uiState.asStateFlow()

    fun onChangeDate() {

    }

    fun setUiState(date: LocalDate = LocalDate.now()) {
        _uiState.value = date
    }

}