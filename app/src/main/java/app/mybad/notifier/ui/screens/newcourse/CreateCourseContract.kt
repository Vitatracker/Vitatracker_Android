package app.mybad.notifier.ui.screens.newcourse

import app.mybad.data.models.DateCourseLimit
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class CreateCourseContract {
    sealed interface Event : ViewEvent {
        //        data class SetUsage(val usage: UsageDomainModel) : Event
        data class UpdateRemedy(val remedy: RemedyDomainModel) : Event
        data class UpdateRemedyName(val newName: String) : Event
        data class UpdateCourse(val course: CourseDomainModel) : Event
        data class UpdateUsages(val usages: List<UsageDomainModel>) : Event
        data class UpdateUsagePatterns(val patterns: List<UsageFormat>) : Event

        object AddUsagesPattern : Event
        data class DeleteUsagePattern(val pattern: UsageFormat) : Event
        data class ChangeQuantityUsagePattern(val pattern: UsageFormat, val quantity: Int) : Event
        data class ChangeTimeUsagePattern(val pattern: UsageFormat, val time: Int) : Event

        object CourseIntervalEntered : Event
        object UpdateCourseStartDateAndLimit : Event
        object Drop : Event
        object Finish : Event
        object ActionBack : Event
        object ActionNext : Event
        object ActionCollapse : Event
        object ActionExpand : Event
    }

    data class State(
        val remedy: RemedyDomainModel = RemedyDomainModel(),
        val course: CourseDomainModel = CourseDomainModel(),
        val usages: List<UsageDomainModel> = emptyList(),
        val usagesPattern: List<UsageFormat> = emptyList(),
        val isError: Boolean = false,
        val courseIntervalEntered: Boolean = false,
        val nextAllowed: Boolean = false,
        val dateLimit: DateCourseLimit,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        object Collapse : Effect
        object Expand : Effect
        sealed interface Navigation : Effect {
            object Next : Navigation
            object Back : Navigation
        }
    }
}
