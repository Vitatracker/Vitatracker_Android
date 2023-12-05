package app.mybad.notifier.ui.screens.newcourse

import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import kotlinx.datetime.LocalDateTime

class CreateCourseContract {
    sealed interface Event : ViewEvent {
        //        data class SetUsage(val usage: UsageDomainModel) : Event
        data class UpdateRemedy(val remedy: RemedyDomainModel) : Event
        data class UpdateRemedyName(val newName: String) : Event
        data class UpdateCourse(val course: CourseDomainModel) : Event
        data class UpdateCourseRemindDate(val remindDate: LocalDateTime?, val interval: Long) : Event
        data class UpdateUsages(val usages: List<UsageDomainModel>) : Event
        data class UpdateUsagePatterns(val patterns: List<UsageFormat>) : Event

        data object AddUsagesPattern : Event
        data class DeleteUsagePattern(val pattern: UsageFormat) : Event
        data class ChangeQuantityUsagePattern(val pattern: UsageFormat, val quantity: Float) : Event
        data class ChangeTimeUsagePattern(val pattern: UsageFormat, val time: Int) : Event

        data object UpdateCourseStartDate : Event
        data object Drop : Event
        data object Finish : Event
        data object ActionBack : Event
        data class ConfirmBack(val confirm: Boolean) : Event
        data object Cancel : Event
        data object ActionNext : Event
        data object ActionCollapse : Event
        data object ActionExpand : Event
    }

    data class State(
        val remedy: RemedyDomainModel = RemedyDomainModel(),
        val course: CourseDomainModel = CourseDomainModel(),
        val usages: List<UsageDomainModel> = emptyList(),

        val usagesPattern: List<UsageFormat> = emptyList(),

        val isError: Boolean = false,
        val courseIntervalEntered: Boolean = false,
        val updateCourseStartDate: Boolean = false,
        val nextAllowed: Boolean = false,
        val confirmBack: Boolean = false,
        val loader: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        data object Collapse : Effect
        data object Expand : Effect
        data class Toast(val message: String) : Effect
        sealed interface Navigation : Effect {
            data object Next : Navigation
            data object Back : Navigation
        }
    }
}
