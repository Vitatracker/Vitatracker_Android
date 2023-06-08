package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.data.room.MedDAO
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.repos.CoursesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursesRepoImpl @Inject constructor(
    private val db: MedDAO
) : CoursesRepo {

    override suspend fun getAll(userId: Long): List<CourseDomainModel> {
        return db.getAllCourses(userId).mapToDomain()
    }

    override suspend fun getAllFlow(userId: Long): Flow<List<CourseDomainModel>> {
        return db.getAllCoursesFlow(userId).map { it.mapToDomain() }
    }

    override suspend fun getSingle(courseId: Long): CourseDomainModel {
        return db.getCourseById(courseId).mapToDomain()
    }

    override suspend fun updateSingle(courseId: Long, item: CourseDomainModel) {
        db.addCourse(item.copy(id = courseId).mapToData())
    }

    override suspend fun add(item: CourseDomainModel) {
        db.addCourse(item.mapToData())
    }

    override suspend fun deleteSingle(courseId: Long) {
        db.deleteCourse(courseId)
    }
}
