package app.mybad.notifier.ui.screens.calender

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel

data class CalendarState(
    val courses: List<CourseDomainModel> = emptyList(),
    val usages: List<UsagesDomainModel> = emptyList(),
    val meds: List<MedDomainModel> = emptyList(),
)

sealed interface CalendarIntent {
    data class SetUsage(val medId: Long, val usageTime: Long, val factUsageTime: Long) : CalendarIntent
}