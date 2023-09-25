package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.UsageRepository
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class CloseCourseUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val patternUsageRepository: PatternUsageRepository,
    private val usageRepository: UsageRepository,
) {

    // закрыть курс, поставить дату завершения и удалить usages после даты
    suspend operator fun invoke(courseId: Long, endDate: LocalDateTime) {
        courseRepository.getCourseById(courseId).onSuccess { course ->
            courseRepository.updateCourse(
                course.copy(
                    endDate = endDate,// тут с учетом часового пояса, в мапере преобразуется в UTC
                    notUsed = true,
                    isFinished = true,
                    updateNetworkDate = 0,
                )
            )
        }
        // пометить паттерн как закрытый
        patternUsageRepository.finishedPatternUsageByCourseId(courseId)
        // удалить usage после даты закрытия
        usageRepository.markDeletionUsagesAfterByCourseId(courseId)
    }
}
