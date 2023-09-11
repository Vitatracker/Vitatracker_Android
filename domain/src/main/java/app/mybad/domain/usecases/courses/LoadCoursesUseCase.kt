package app.mybad.domain.usecases.courses

import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val patternUsageRepository: PatternUsageRepository,
) {

    operator fun invoke(
        date: Long,
        userId: Long = AuthToken.userId,
    ) =
        combine(
            courseRepository.getCoursesWithParams(userId)
                .distinctUntilChanged()
                .map { courses ->
                    addRemindCourse(courses, date)
                },
            patternUsageRepository.getPatternUsages(userId),
            ::Pair
        )

    private fun addRemindCourse(
        courses: List<CourseDisplayDomainModel>,
        date: Long
    ): List<CourseDisplayDomainModel> {
        val newCourses = mutableListOf<CourseDisplayDomainModel>()
        courses.forEach { newCourse ->
            if (newCourse.remindDate > 0) {
                val startDate = newCourse.endDate + newCourse.interval
                if (startDate > date) {
                    val endDate = startDate + (newCourse.endDate - newCourse.startDate)
                    newCourses.add(
                        newCourse.copy(
                            idn = 0,
                            startDate = startDate,
                            endDate = endDate,
                            remindDate = -1,
                            interval = startDate - date,
                        )
                    )
                }
            }
        }
        return courses.plus(newCourses)
    }
}
