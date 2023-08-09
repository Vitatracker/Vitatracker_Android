package app.mybad.data.repos

import app.mybad.data.db.dao.CourseDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.CourseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CourseRepositoryImpl @Inject constructor(
    private val db: CourseDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CourseRepository {

    override fun getCourses(userId: Long) = db.getCourses(userId)
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun getCoursesByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getCoursesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getCoursesByRemedyId(remedyId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getCoursesByRemedyId(remedyId).mapToDomain()
        }
    }

    override suspend fun getCourseById(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getCourseById(courseId).mapToDomain()
        }
    }

    override suspend fun getCourseByIdn(courseIdn: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getCourseByIdn(courseIdn).mapToDomain()
        }
    }

    override suspend fun insertCourse(course: CourseDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            db.insertCourse(course.mapToData())
        }
    }

    override suspend fun insertCourse(courses: List<CourseDomainModel>) = withContext(dispatcher) {
        Result.runCatching {
            db.insertCourse(courses.mapToData())
        }
    }

    override suspend fun updateCourse(course: CourseDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            db.insertCourse(course.mapToData())
        }
    }

    override suspend fun deleteCourse(courses: List<CourseDomainModel>) = withContext(dispatcher) {
        Result.runCatching {
            db.deleteCourse(courses.mapToData())
        }
    }

    override suspend fun delete(courseId: Long, dateTime: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.delete(courseId,dateTime)
        }
    }

    override suspend fun deleteCourseById(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.deleteCourseById(courseId)
        }
    }

    override suspend fun getCoursesNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getCoursesNotUpdateByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getCoursesDeletedByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getCoursesDeletedByUserId(userId).mapToDomain()
        }
    }

}
