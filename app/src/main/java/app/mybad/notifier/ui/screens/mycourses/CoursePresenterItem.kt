package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.plusDay
import app.mybad.notifier.utils.secondsToDay
import app.mybad.notifier.utils.toEpochSecond

data class CoursePresenterItem(val med: MedDomainModel, val course: CourseDomainModel, val usages: List<UsageCommonDomainModel>) {
    private val now = getCurrentDateTime().toEpochSecond()
    val itemsCount = if (usages.isNotEmpty()) {
        val firstCount = usages.first().quantity
        val firstTime = usages.first().useTime
        if (usages.filter { it.useTime <= (firstTime.plusDay()) }
                .all { it.quantity == firstCount }) firstCount else 0
    } else {
        0
    }
    val usagesCount = usages.count { it.useTime < (usages.first().useTime.plusDay()) }
    val startInDays = (course.startDate + course.interval - now).secondsToDay().toInt()
}
