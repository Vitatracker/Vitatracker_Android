package app.mybad.notifier.ui.screens.newcourse

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class CreateCourseScreensContract {

    sealed class Event : ViewEvent {
        object ActionBack : Event()
        object ActionNext : Event()
        object Drop : Event()
        object Finish : Event()
        object CourseIntervalEntered : Event()
        data class UpdateMedName(val newName: String) : Event()
        data class UpdateMed(val med: MedDomainModel) : Event()
        data class UpdateCourse(val course: CourseDomainModel) : Event()
        data class UpdateUsagePattern(val index: Int, val pattern: Pair<Long, Int>) : Event()
        data class AddUsagesPattern(val pattern: Pair<Long, Int>) : Event()
        data class RemoveUsagesPattern(val index: Int) : Event()
        data class UpdateUsages(val usages: List<UsageCommonDomainModel>) : Event()
    }

    data class State(
        val med: MedDomainModel = MedDomainModel(),
        val course: CourseDomainModel = CourseDomainModel(),
        val usages: List<UsageCommonDomainModel> = emptyList(),
        val usagesPattern: List<Pair<Long, Int>> = emptyList(),
        val isError: Boolean = false,
        val courseIntervalEntered: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object ActionBack : Navigation()
            object ActionNext : Navigation()
        }
    }
}