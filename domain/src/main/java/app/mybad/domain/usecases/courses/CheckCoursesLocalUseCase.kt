package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class CheckCoursesLocalUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    ) {

    // проверить целостность локальной базы по id
    // remedies->courses->usages
    // courses->usages
    // usages
    suspend operator fun invoke() {
        Log.d("VTTAG", "SynchronizationCourseWorker::CheckCoursesLocalUseCase: Start")
        // TODO("сделать проверку целостности локальной базы")
        Log.d("VTTAG", "SynchronizationCourseWorker::CheckCoursesLocalUseCase: End")
    }

}
