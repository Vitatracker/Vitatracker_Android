package app.mybad.notifier.ui.screens.calender

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel

data class CalendarState(
    val remedies: List<RemedyDomainModel> = emptyList(),
    val courses: List<CourseDomainModel> = emptyList(),
    val usages: List<UsageDomainModel> = emptyList(),
)

sealed interface CalendarIntent {
    data class SetUsage(val usage: UsageDomainModel) : CalendarIntent
}
