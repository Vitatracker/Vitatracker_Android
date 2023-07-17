package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LoadCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val remedyRepository: RemedyRepository,
    private val usageRepository: UsageRepository,
) {

    operator fun invoke(
        userId: Long
    ) =
        combine(
            courseRepository.getCourses(userId),
            remedyRepository.getRemedies(userId),
            usageRepository.getUsages(userId),
            ::Triple
        ).onEach { (courses, remedies, usages) ->
            Log.w(
                "VTTAG",
                "LoadCoursesUseCase: courses=${courses.size} remedies=${remedies.size} usages=${usages.size}"
            )
        }

}
