package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.notifier.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val coursesList = listOf(
    CourseDomainModel(id=1L, medId = 1L, startDate = 0L, endDate = 11000000L),
    CourseDomainModel(id=2L, medId = 2L, startDate = 0L, endDate = 12000000L),
    CourseDomainModel(id=3L, medId = 3L, startDate = 0L, endDate = 13000000L),
)

val medsList = listOf(
    MedDomainModel(id=1L, name = "Doliprane",   details = MedDetailsDomainModel(type = 1, dose = 500, measureUnit = 1, icon = R.drawable.pill)),
    MedDomainModel(id=2L, name = "Dexedrine",   details = MedDetailsDomainModel(type = 1, dose = 30,  measureUnit = 1, icon = R.drawable.pill)),
    MedDomainModel(id=3L, name = "Prozac",      details = MedDetailsDomainModel(type = 1, dose = 120, measureUnit = 1, icon = R.drawable.pill)),
)

val usages = listOf(
    UsageDomainModel(1677182682L),
    UsageDomainModel(1677182683L),
    UsageDomainModel(1677254684L),
    UsageDomainModel(1677269085L),
    UsageDomainModel(1677355486L),
    UsageDomainModel(1677341087L),
    UsageDomainModel(1677427488L),
)
val usages1 = listOf(
    UsageDomainModel(1677182662L),
    UsageDomainModel(1677182663L),
    UsageDomainModel(1677254664L),
    UsageDomainModel(1677269065L),
    UsageDomainModel(1677355466L),
    UsageDomainModel(1677341067L),
    UsageDomainModel(1677427468L),
)
val usages2 = listOf(
    UsageDomainModel(1677182681L),
    UsageDomainModel(1677182682L),
    UsageDomainModel(1677254683L),
    UsageDomainModel(1677269084L),
    UsageDomainModel(1677355485L),
)

val usagesList = listOf(
    UsagesDomainModel(medId = 1L, usages = usages),
    UsagesDomainModel(medId = 2L, usages = usages1),
    UsagesDomainModel(medId = 3L, usages = usages2),
)

class CalendarViewModel(
    //courses repo
    //meds repo
    //usages repo
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState(
        courses = coursesList,
        meds = medsList,
        usages = usagesList,
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