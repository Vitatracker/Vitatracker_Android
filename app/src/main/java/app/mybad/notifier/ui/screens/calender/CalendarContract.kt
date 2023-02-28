package app.mybad.notifier.ui.screens.calender

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel

data class CalendarState(
    val courses: List<CourseDomainModel> = emptyList(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
    val meds: List<MedDomainModel> = emptyList(),
)

sealed interface CalendarIntent {
    data class SetUsage(val usage: UsageCommonDomainModel) : CalendarIntent
}