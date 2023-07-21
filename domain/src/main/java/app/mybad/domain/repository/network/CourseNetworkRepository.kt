package app.mybad.domain.repository.network

import app.mybad.domain.models.CourseDomainModel

interface CourseNetworkRepository {

    suspend fun getCourse(courseId: Long): Result<CourseDomainModel>
    suspend fun getCoursesByRemedyId(remedyId: Long): Result<List<CourseDomainModel>>
    suspend fun updateCourse(course: CourseDomainModel): Result<CourseDomainModel>
    suspend fun deleteCourse(courseId: Long): Result<Boolean>

    /*
        val result: StateFlow<ApiResult>
        suspend fun getUserModel()
        suspend fun getAll(): ApiResult
        suspend fun updateUsage(usage: UsageDomainModel)
        suspend fun updateAll(
            remedy: RemedyDomainModel,
            course: CourseDomainModel,
            usages: List<UsageDomainModel>
        )
        suspend fun deleteMed(medId: Long)
    */
}
