package app.mybad.notifier.ui.screens.calender

import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.WEEKS_PER_MONTH
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.firstDayOfMonth
import app.mybad.utils.initWeekAndDayOfMonth
import kotlinx.datetime.LocalDateTime

class CalendarContract {
    sealed interface Event : ViewEvent {
        data class SetUsage(val usage: UsageDisplayDomainModel) : Event
        data class ChangeMonth(val date: LocalDateTime) : Event
        data class SelectDate(val date: LocalDateTime?) : Event
        data class SelectElement(val element: Pair<Int, Int>?) : Event
        data object ActionBack : Event
    }

    data class State(
        val date: LocalDateTime = currentDateTimeSystem().firstDayOfMonth(), // с учетом часового пояса

        val selectedElement: Pair<Int, Int>? = null,

        val updateDateWeek: LocalDateTime? = null,

        val datesWeeks: Array<Array<LocalDateTime>> = initWeekAndDayOfMonth(date),
        val usagesWeeks: Array<Array<List<UsageDisplayDomainModel>>> = Array(WEEKS_PER_MONTH) {
            Array(DAYS_A_WEEK) { emptyList() }
        },
    ) : ViewState {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (date != other.date) return false
            if (selectedElement != other.selectedElement) return false
            if (updateDateWeek != other.updateDateWeek) return false

            return true
        }

        override fun hashCode(): Int {
            var result = date.hashCode()
            result = 31 * result + (selectedElement?.hashCode() ?: 0)
            result = 31 * result + (updateDateWeek?.hashCode() ?: 0)
            return result
        }
    }

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object Back : Navigation
        }
    }
}
