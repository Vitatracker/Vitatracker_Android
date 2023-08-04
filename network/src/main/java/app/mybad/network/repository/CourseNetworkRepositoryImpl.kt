package app.mybad.network.repository

import android.util.Log
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.network.api.CourseApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.mapToNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CourseNetworkRepositoryImpl @Inject constructor(
    private val courseApi: CourseApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CourseNetworkRepository {

    override suspend fun getCourses() = withContext(dispatcher) {
        Result.runCatching {
            courseApi.getCourses().mapToDomain()
        }
    }

    override suspend fun getCourse(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            courseApi.getCourse(courseId).mapToDomain()
        }
    }

    override suspend fun getCoursesByRemedyId(
        remedyId: Long,
        remedyIdLoc: Long,
    ) = withContext(dispatcher) {
        Result.runCatching {
            courseApi.getCoursesByRemedyId(remedyId).mapToDomain(
                remedyIdLoc = remedyIdLoc
            )
        }
    }

    override suspend fun updateCourse(course: CourseDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::CourseNetworkRepositoryImpl: course id=${course.id}"
            )
            var courseNet = course.mapToNet()
            courseNet = if (courseNet.id > 0) courseApi.updateCourse(courseNet)
            else courseApi.addCourse(courseNet)
            courseNet.mapToDomain(
                remedyIdLoc = course.remedyId,
                courseIdLoc = course.id,
            )
        }
    }

    override suspend fun deleteCourse(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            courseApi.deleteCourse(courseId).isSuccessful
        }
    }

}
