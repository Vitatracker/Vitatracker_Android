package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LoadCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val remedyRepository: RemedyRepository,
    private val usageRepository: UsageRepository,
) {

    operator fun invoke(
        userId: Long = AuthToken.userId,
    ) =
        combine(
            courseRepository.getCourses(userId).map { (courses, date) ->
                addRemindCourse(courses, date)
            },
            remedyRepository.getRemedies(userId),
            usageRepository.getUsages(userId),
            ::Triple
        ).onEach { (courses, remedies, usages) ->
            Log.w(
                "VTTAG",
                "LoadCoursesUseCase: courses=${courses.size} remedies=${remedies.size} usages=${usages.size}"
            )
        }

    private fun addRemindCourse(
        courses: List<CourseDomainModel>,
        date: Long
    ): List<CourseDomainModel> {
        val newCourses = mutableListOf<CourseDomainModel>()
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
        return if (newCourses.isNotEmpty()) courses.plus(newCourses) else courses
    }
}

/*
                // Отображение планируемого нового курса remindDate > 0
                state.courses.forEach { newCourse ->
                    if (newCourse.remindDate > 0 && newCourse.remindDate >= now) {
                        item {
                            val startDate = newCourse.endDate + newCourse.interval
                            val endDate = startDate + (newCourse.endDate - newCourse.startDate)
                            CourseItem(
                                course = newCourse.copy(
                                    id = 0,
                                    idn = 0,
                                    startDate = startDate,
                                    endDate = endDate,
                                    remindDate = 0,
                                    interval = 0,
                                ),
                                remedy = state.remedies.first { it.id == newCourse.remedyId },
                                usages = state.usages.filter { usage ->
                                    usage.courseId == newCourse.id &&
                                            usage.useTime >= newCourse.startDate &&
                                            usage.useTime < newCourse.startDate.plusDay()
                                }.take(10),
                                startInDays = (startDate - now).secondsToDay().toInt(),
                            )
                        }
                    }
                }

 */
