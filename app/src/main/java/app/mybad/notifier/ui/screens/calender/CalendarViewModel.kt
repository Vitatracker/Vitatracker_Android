package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.notifier.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject




@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val courses: CoursesRepo,
    private val usages: UsagesRepo,
    private val meds: MedsRepo
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState(
        courses = courses.getAll(),
        meds = meds.getAll(),
        usages = usages.getAll(),
    ))
    val state get() = _state.asStateFlow()

    fun reducer(intent: CalendarIntent) {
        when(intent) {
            is CalendarIntent.SetUsage -> {
                Log.w("CVM_", "usage set: ${intent.usageTime} at ${intent.factUsageTime} with med ${intent.medId}")
            }
        }
    }

}