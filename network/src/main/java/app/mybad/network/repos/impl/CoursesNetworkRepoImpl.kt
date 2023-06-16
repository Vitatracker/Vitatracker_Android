package app.mybad.network.repos.impl

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.utils.ApiResult
import app.mybad.network.api.CoursesApi
import app.mybad.network.models.UserModel
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.mapToNet
import app.mybad.network.models.response.Remedies
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.network.utils.ApiHandler.handleApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Named

class CoursesNetworkRepoImpl @Inject constructor(
    @Named("c_api") private val coursesApi: CoursesApi,
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
    private val medsRepo: MedsRepo,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CoursesNetworkRepo {
    private val _result = MutableStateFlow<ApiResult>(ApiResult.ApiSuccess(""))
    override val result: StateFlow<ApiResult> = _result.asStateFlow()
    override suspend fun getUserModel() {
        withContext(dispatcher) {
            try {
                // TODO("проверить логику с userId")
                if (AuthToken.userId != -1L) {
                    val r = handleApi { coursesApi.getUserModel(AuthToken.userId).execute() }
                    if (r is ApiResult.ApiSuccess && r.data is UserModel) {
                        (r.data as UserModel).remedies?.forEach { remedies ->
                            medsRepo.add(remedies.mapToDomain())
                            remedies.courses?.forEach { courses ->
                                coursesRepo.add(courses.mapToDomain(remedies.userId))
                                courses.usages?.mapToDomain(courses.remedyId, remedies.userId)
                                    ?.let {
                                        usagesRepo.addUsages(it)
                                    }
                            }
                        }
                    } else {
                    }
                } else {
                    ApiResult.ApiError(666, "null user id")
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    override suspend fun getAll() {
        withContext(dispatcher) {
            try {
                // TODO("проверить логику с userId")
                if (AuthToken.userId != -1L) {
                    val r = handleApi { coursesApi.getAll().execute() }
                    Log.w("CNRI", "api result: $r")
                    if (
                        r is ApiResult.ApiSuccess &&
                        r.data is List<*> &&
                        (r.data as List<*>).isNotEmpty() &&
                        (r.data as List<*>).first() is Remedies
                    ) {
                        @Suppress("UNCHECKED_CAST")
                        (r.data as List<Remedies>).forEach { remedies ->
                            Log.w("CNRI", "remedy: $remedies")
                            medsRepo.add(remedies.mapToDomain())
                            remedies.courses?.forEach { courses ->
                                Log.w("CNRI", "course: $courses")
                                // TODO("проверить логику с userId")
                                coursesRepo.add(courses.mapToDomain(AuthToken.userId))
                                courses.usages?.mapToDomain(courses.remedyId, AuthToken.userId)
                                    ?.let {
                                        usagesRepo.addUsages(it)
                                    }
                            }
                        }
                    } else {
                    }
                } else {
                    ApiResult.ApiError(666, "null user id")
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    override suspend fun updateUsage(usage: UsageCommonDomainModel) {
        // TODO("проверить логику с userId")
        val a = coursesRepo.getAll(usage.userId).first { it.medId == usage.medId }
        val u = usage.mapToNet(a.id)
        execute { coursesApi.updateUsage(u, u.id) }
    }

    override suspend fun updateAll(
        med: MedDomainModel,
        course: CourseDomainModel,
        usages: List<UsageCommonDomainModel>
    ) {
        Log.w(
            "VTTAG",
            "CoursesNetworkRepoImpl:updateAll - userId=${AuthToken.userId} updating ${med.name} #${course.id}"
        )
        val courses = course.mapToNet(usages)
        // TODO("проверить логику с userId")
        val remedies = Remedies(
            id = med.id,
            userId = AuthToken.userId,
            name = med.name.toString(),
            description = med.description.toString(),
            comment = med.comment.toString(),
            type = med.type,
            icon = med.icon.toLong(),
            color = med.color.toLong(),
            dose = med.dose.toLong(),
            measureUnit = med.measureUnit,
            beforeFood = med.beforeFood,
            photo = "",
            historyRemedys = emptyList(),
            courses = listOf(courses)
        )
        execute { coursesApi.addAll(remedies) }
    }

    override suspend fun deleteMed(medId: Long) {
        execute { coursesApi.deleteMed(medId) }
    }

    private suspend fun execute(request: () -> Call<*>): ApiResult = withContext(dispatcher) {
        when (val response = handleApi { request.invoke().execute() }) {
            is ApiResult.ApiSuccess -> ApiResult.ApiSuccess(data = response.data)
            is ApiResult.ApiError -> ApiResult.ApiError(
                code = response.code,
                message = response.message
            )

            is ApiResult.ApiException -> ApiResult.ApiException(e = response.e)
        }
    }
}
