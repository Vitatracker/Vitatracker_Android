package app.mybad.domain.repository

import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.models.CourseDomainModel
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCoursesWithParams(userId: Long): Flow<List<CourseDisplayDomainModel>>
    fun getCourses(userId: Long): Flow<Pair<List<CourseDomainModel>, Long>>
    suspend fun getCoursesByUserId(userId: Long): Result<List<CourseDomainModel>>
    suspend fun getCoursesByRemedyId(remedyId: Long): Result<List<CourseDomainModel>>
    suspend fun getCourseById(courseId: Long): Result<CourseDomainModel>
    suspend fun getCourseByIdn(courseIdn: Long): Result<CourseDomainModel>
    suspend fun getCoursesWithRemindDateBetween(
        userId: Long,
        startTime: Long,
        endTime: Long,
    ): Result<List<CourseDisplayDomainModel>>
    suspend fun getCourseWithParamsById(courseId: Long): Result<CourseDisplayDomainModel>
    suspend fun countActiveCourses(userId: Long): Result<Long>

    suspend fun insertCourse(course: CourseDomainModel): Result<Long>
    suspend fun insertCourse(courses: List<CourseDomainModel>): Result<Unit>
    suspend fun updateCourse(course: CourseDomainModel): Result<Long>

    suspend fun markDeletionCourseById(courseId: Long): Result<Unit>

    suspend fun deleteCoursesById(courseId: Long): Result<Unit>
    suspend fun deleteCoursesByUserId(userId: Long): Result<Unit>
    suspend fun deleteCourse(courses: List<CourseDomainModel>): Result<Unit>

    suspend fun getCoursesNotUpdateByUserId(userId: Long): Result<List<CourseDomainModel>>
    suspend fun getCoursesDeletedByUserId(userId: Long): Result<List<CourseDomainModel>>
}
