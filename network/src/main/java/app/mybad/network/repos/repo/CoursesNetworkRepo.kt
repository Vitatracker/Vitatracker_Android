package app.mybad.network.repos.repo

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.utils.ApiResult
import kotlinx.coroutines.flow.StateFlow

interface CoursesNetworkRepo {

    val result: StateFlow<ApiResult>
    suspend fun getUserModel()
    suspend fun getAll()
    suspend fun updateUsage(usage: UsageCommonDomainModel)
    suspend fun updateCourse(course: CourseDomainModel)
    suspend fun updateAll(
        med: MedDomainModel,
        course: CourseDomainModel,
        usages: List<UsageCommonDomainModel>
    )
    suspend fun addUsages(usages: List<UsageCommonDomainModel>)
    suspend fun addCourse(course: CourseDomainModel)
    suspend fun addMed(med: MedDomainModel)
    suspend fun deleteUsage(usageId: Long)
    suspend fun deleteCourse(courseId: Long)
    suspend fun deleteMed(medId: Long)
}
