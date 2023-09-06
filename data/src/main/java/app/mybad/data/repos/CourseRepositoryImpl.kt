package app.mybad.data.repos

import app.mybad.data.db.dao.CourseDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.CourseRepository
import app.mybad.utils.currentDateTimeInSecond
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
        .map { it.mapToDomain() to currentDateTimeInSecond() }
        .flowOn(dispatcher)

    override suspend fun getCoursesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getCoursesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getCoursesByRemedyId(remedyId: Long) = withContext(dispatcher) {
        runCatching {
            db.getCoursesByRemedyId(remedyId).mapToDomain()
        }
    }

    override suspend fun getCourseById(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.getCourseById(courseId).mapToDomain()
        }
    }

    override suspend fun getCourseByIdn(courseIdn: Long) = withContext(dispatcher) {
        runCatching {
            db.getCourseByIdn(courseIdn).mapToDomain()
        }
    }

    override suspend fun insertCourse(course: CourseDomainModel) = withContext(dispatcher) {
        runCatching {
            db.insertCourse(course.mapToData())
        }
    }

    override suspend fun insertCourse(courses: List<CourseDomainModel>) = withContext(dispatcher) {
        runCatching {
            db.insertCourse(courses.mapToData())
        }
    }

    override suspend fun updateCourse(course: CourseDomainModel) = insertCourse(course)

    override suspend fun markDeletionCourseById(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.markDeletionCourseById(courseId)
        }
    }

    override suspend fun deleteCoursesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteCoursesByUserId(userId)
        }
    }

    override suspend fun deleteCoursesById(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteCoursesById(courseId)
        }
    }

    override suspend fun deleteCourse(courses: List<CourseDomainModel>) = withContext(dispatcher) {
        runCatching {
            db.deleteCourse(courses.mapToData())
        }
    }

    override suspend fun getCoursesNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getCoursesNotUpdateByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getCoursesDeletedByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getCoursesDeletedByUserId(userId).mapToDomain()
        }
    }

}
