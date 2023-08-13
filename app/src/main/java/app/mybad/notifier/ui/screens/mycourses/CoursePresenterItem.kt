package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.plusDay
import app.mybad.utils.secondsToDay

data class CoursePresenterItem(
    val remedy: RemedyDomainModel,
    val course: CourseDomainModel,
    val usages: List<UsageDomainModel>
) {
    private val now = currentDateTimeInSecond()
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
