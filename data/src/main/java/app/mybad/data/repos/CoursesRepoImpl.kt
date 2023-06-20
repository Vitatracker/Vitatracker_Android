package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.data.db.dao.MedDao
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.repos.CoursesRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CoursesRepoImpl @Inject constructor(
    private val db: MedDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CoursesRepo {

    override suspend fun getAll(userId: Long): List<CourseDomainModel> = withContext(dispatcher) {
        db.getAllCourses(userId).mapToDomain()
    }

    override fun getAllFlow(userId: Long): Flow<List<CourseDomainModel>> =
        db.getAllCoursesFlow(userId)
            .map { it.mapToDomain() }
            .flowOn(dispatcher)

    override suspend fun getSingle(courseId: Long): CourseDomainModel = withContext(dispatcher) {
        db.getCourseById(courseId).mapToDomain()
    }

    override suspend fun updateSingle(courseId: Long, item: CourseDomainModel) {
        withContext(dispatcher) {
            db.addCourse(item.copy(id = courseId).mapToData())
        }
    }

    override suspend fun add(item: CourseDomainModel): Long? = withContext(dispatcher) {
        db.addCourse(item.mapToData())
    }

    override suspend fun deleteSingle(courseId: Long) {
        withContext(dispatcher) {
            db.deleteCourse(courseId)
        }
    }
}
