package app.mybad.domain.usecases.courses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesNetworkRepo
import javax.inject.Inject

class UpdateCourseAllUseCase @Inject constructor(
    private val repository: CoursesNetworkRepo,
) {

    suspend operator fun invoke(
        med: MedDomainModel,
        course: CourseDomainModel,
        usages: List<UsageCommonDomainModel>
    ) {
        repository.updateAll(med = med, course = course, usages = usages)
    }
}
