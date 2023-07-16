package app.mybad.domain.repository

import app.mybad.domain.models.CourseDomainModel
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCourses(userId: Long): Flow<List<CourseDomainModel>>
    suspend fun getCoursesByUserId(userId: Long): Result<List<CourseDomainModel>>
    suspend fun getCourseById(courseId: Long): Result<CourseDomainModel>
    suspend fun insertCourse(course: CourseDomainModel): Result<Long?>
    suspend fun updateCourse(course: CourseDomainModel)
    suspend fun deleteCourseById(courseId: Long)
    suspend fun getCoursesNotUpdateByUserId(userId: Long): Result<List<CourseDomainModel>>
}
