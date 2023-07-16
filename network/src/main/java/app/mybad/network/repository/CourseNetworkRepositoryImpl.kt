package app.mybad.network.repository

import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.network.api.CourseApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class CourseNetworkRepositoryImpl @Inject constructor(
    private val coursesApi: CourseApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CourseNetworkRepository {

}
/*
class CoursesNetworkRepoImpl @Inject constructor(
    private val coursesApi: CoursesApi,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val remedyRepository: RemedyRepository,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CoursesNetworkRepo {
    private val _result = MutableStateFlow<ApiResult>(ApiResult.ApiSuccess(""))
    override val result: StateFlow<ApiResult> = _result.asStateFlow()
    override suspend fun getUserModel() {
        withContext(dispatcher) {
            try {
                // TODO("проверить логику с userId")
                if (AuthToken.userId != -1L) {
                    val r = ApiHandler.handleApi {
                        coursesApi.getUserNetworkModel(AuthToken.userId).execute()
                    }
                    if (r is ApiResult.ApiSuccess && r.data is UserNetworkModel) {
                        (r.data as UserNetworkModel).remedies?.forEach { remedies ->
                            remedyRepository.insertRemedy(remedies.mapToDomain())
                            remedies.courses?.forEach { courses ->
                                courseRepository.insertCourse(courses.mapToDomain(remedies.userId))
                                courses.usages?.mapToDomain(courses.remedyId, remedies.userId)
                                    ?.let {
                                        usageRepository.addUsages(it)
                                    }
                            }
                        }
                    } else {
                    }
                } else {
                    ApiResult.ApiError(777, "null user id")
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    override suspend fun getAll(): ApiResult = withContext(dispatcher) {
        try {
            // TODO("проверить логику с userId")
            if (AuthToken.userId != -1L) {
                val r = ApiHandler.handleApi { coursesApi.getRemedies().execute() }
                Log.w("CNRI", "api result: $r")
                if (r is ApiResult.ApiSuccess && r.data is List<*>) {
                    val list = r.data as? List<RemedyNetworkModel> ?: error("")
                    list.forEach { remedies ->
                        Log.w("CNRI", "remedy: $remedies")
                        remedyRepository.insertRemedy(remedies.mapToDomain())
                        remedies.courses?.forEach { courses ->
                            Log.w("CNRI", "course: $courses")
                            // TODO("проверить логику с userId")
                            courseRepository.insertCourse(courses.mapToDomain(userId = AuthToken.userId))
                            courses.usages?.mapToDomain(
                                medId = courses.remedyId,
                                userId = AuthToken.userId
                            )?.let {
                                usageRepository.addUsages(it)
                            }
                        }
                    }
                    ApiResult.ApiSuccess(data = 0)
                } else error("")
            } else ApiResult.ApiError(ERROR_NETWORK, "null user id")
        } catch (t: Throwable) {
            t.printStackTrace()
            ApiResult.ApiError(ERROR_NETWORK, "Error: online update failed!")
        }
    }

    override suspend fun updateUsage(usage: UsageDomainModel) {
        // TODO("проверить логику с userId")
        val a = courseRepository.getCoursesByUserId(usage.userId)
            .firstOrNull { it.medId == usage.medId } ?: return
        val u = usage.mapToNet(a.id)
        execute { coursesApi.updateUsage(u, u.id) }
    }

    override suspend fun updateAll(
        remedy: RemedyDomainModel,
        course: CourseDomainModel,
        usages: List<UsageDomainModel>
    ) {
        Log.w(
            "VTTAG",
            "CoursesNetworkRepoImpl:updateAll - userId=${AuthToken.userId} updating ${remedy.name} #${course.id}"
        )
        val courses = course.mapToNet(usages)
        // TODO("проверить логику с userId")
        val remedies = RemedyNetworkModel(
            id = remedy.id,
            userId = AuthToken.userId,
            name = remedy.name.toString(),
            description = remedy.description.toString(),
            comment = remedy.comment.toString(),
            type = remedy.type,
            icon = remedy.icon,
            color = remedy.color,
            dose = remedy.dose,
            measureUnit = remedy.measureUnit,
            beforeFood = remedy.beforeFood,
            photo = "",
        )
        execute { coursesApi.addAll(remedies) }
    }

    override suspend fun deleteMed(medId: Long) {
        execute { coursesApi.deleteMed(medId) }
    }

    private suspend fun execute(request: () -> Call<*>): ApiResult = withContext(dispatcher) {
        when (val response = ApiHandler.handleApi { request.invoke().execute() }) {
            is ApiResult.ApiSuccess -> ApiResult.ApiSuccess(data = response.data)
            is ApiResult.ApiError -> ApiResult.ApiError(
                code = response.code,
                message = response.message
            )

            is ApiResult.ApiException -> ApiResult.ApiException(e = response.e)
        }
    }
}
*/
