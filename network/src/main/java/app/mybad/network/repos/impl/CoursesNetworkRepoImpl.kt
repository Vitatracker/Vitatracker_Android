package app.mybad.network.repos.impl

import android.util.Log
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.utils.ApiResult
import app.mybad.network.api.CoursesApi
import app.mybad.network.models.response.Courses
import app.mybad.network.models.response.Remedies
import app.mybad.network.models.response.Usages
import app.mybad.network.models.response.UserModel
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.network.utils.ApiHandler.handleApi
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Singleton
class CoursesNetworkRepoImpl @Inject constructor(
    private val coursesApi: CoursesApi,
    private val dataStoreRepo: DataStoreRepo,
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
    private val medsRepo: MedsRepo,
) : CoursesNetworkRepo {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var token = ""
    private var userId : Long? = null
    private val _result = MutableStateFlow<ApiResult>(ApiResult.ApiSuccess(""))
    override val result: StateFlow<ApiResult> get() = _result
    init {
        scope.launch {
            dataStoreRepo.getToken().collect {
                token = it
                if(token.isNotBlank()) {
                    val b2 = token.split('.')[1]
                    val body = Base64.UrlSafe.decode(b2).decodeToString()
                    val gson = Gson()
                    userId = gson.fromJson(body, Map::class.java)["id"].toString().toLongOrNull()
                }
            }
        }
    }
    override suspend fun getUserModel() {
        try {
            if (userId != null) {
                val r = handleApi { coursesApi.getUserModel(userId!!).execute() }
                if(r is ApiResult.ApiSuccess && r.data is UserModel) {
                    (r.data as UserModel).remedies?.forEach { remedies ->
                        medsRepo.add(remedies.mapToDomain())
                        remedies.courses?.forEach { courses ->
                            coursesRepo.add(courses.mapToDomain(remedies.userId))
                            courses.usages?.mapToDomain(courses.remedyId, remedies.userId)?.let {
                                usagesRepo.addUsages(it)
                            }
                        }
                    }
                }
            } else {
                ApiResult.ApiError(666, "null user id")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override suspend fun getAll() {
        try {
            if (userId != null) {
                val r = handleApi { coursesApi.getAll().execute() }
                Log.w("CNRI", "api result: $r")
                if(
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
                            coursesRepo.add(courses.mapToDomain(userId!!))
                            courses.usages?.mapToDomain(courses.remedyId, userId!!)?.let {
                                usagesRepo.addUsages(it)
                            }
                        }
                    }
                }
            } else {
                ApiResult.ApiError(666, "null user id")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
    override suspend fun updateUsage(usage: UsageCommonDomainModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCourse(course: CourseDomainModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAll(
        med: MedDomainModel,
        course: CourseDomainModel,
        usages: List<UsageCommonDomainModel>
    ) {
        Log.w("CNRI_update", "updating ${med.name} #${course.id}")
        val courses = course.mapToNet(usages)
        val remedies = Remedies(
            id = med.id,
            userId = userId!!,
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

    override suspend fun addUsages(usages: List<UsageCommonDomainModel>) {
        TODO("Not yet implemented")
    }

    override suspend fun addCourse(course: CourseDomainModel) {
        TODO("Not yet implemented")
    }

    override suspend fun addMed(med: MedDomainModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUsage(usageId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCourse(courseId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMed(medId: Long) {
        TODO("Not yet implemented")
    }

    private suspend fun execute(request: () -> Call<*>): ApiResult {
        return when (val response = handleApi { request.invoke().execute() }) {
            is ApiResult.ApiSuccess -> ApiResult.ApiSuccess(data = response.data)
            is ApiResult.ApiError -> ApiResult.ApiError(
                code = response.code,
                message = response.message
            )
            is ApiResult.ApiException -> ApiResult.ApiException(e = response.e)
        }
    }
    private fun Usages.mapToDomain(medId: Long, userId: Long) : UsageCommonDomainModel {
        return UsageCommonDomainModel(
            id = id.toInt(),
            medId = medId,
            userId = userId,
            useTime = useTime,
            factUseTime = factUseTime,
            quantity = quantity,
            isDeleted = notUsed
        )
    }

    private fun List<Usages>.mapToDomain(medId: Long, userId: Long) : List<UsageCommonDomainModel> {
        return mutableListOf<UsageCommonDomainModel>().apply {
            this@mapToDomain.forEach {
                add(it.mapToDomain(medId, userId))
            }
        }.toList()
    }

    private fun Courses.mapToDomain(userId: Long) : CourseDomainModel {
        return CourseDomainModel(
            id = id,
            medId = remedyId,
            userId = userId,
            regime = regime.toInt(),
            startDate = startDate,
            endDate = endDate,
            remindDate = remindDate,
            interval = interval,
            comment = comment,
            isFinished = isFinished,
            isInfinite = isInfinite,
        )
    }

    private fun CourseDomainModel.mapToNet(usages: List<UsageCommonDomainModel>) : Courses {
        return Courses(
            id = id,
            remedyId = medId,
            regime = regime.toLong(),
            startDate = startDate,
            endDate = endDate,
            remindDate = remindDate,
            interval = interval,
            comment = comment,
            isInfinite = isInfinite,
            isFinished = isFinished,
            usages = usages.mapToNet(id),
            notUsed = false
        )
    }

    private fun List<Courses>.mapToDomain(userId: Long) : List<CourseDomainModel> {
        return mutableListOf<CourseDomainModel>().apply {
            this@mapToDomain.forEach {
                add(it.mapToDomain(userId))
            }
        }
    }

    private fun Remedies.mapToDomain() : MedDomainModel {
        return MedDomainModel(
            id = id,
            userId = userId,
            name = name,
            description = description,
            comment = comment,
            type = type,
            dose = dose.toInt(),
            icon = icon.toInt(),
            color = color.toInt(),
            measureUnit = measureUnit,
            beforeFood = beforeFood,
        )
    }

    private fun UsageCommonDomainModel.mapToNet(courseId: Long) : Usages {
        return Usages(
            id = id.toLong(),
            courseId = courseId,
            useTime = useTime,
            factUseTime = factUseTime,
            notUsed = isDeleted,
            quantity = quantity
        )
    }

    private fun List<UsageCommonDomainModel>.mapToNet(courseId: Long) : List<Usages> {
        return mutableListOf<Usages>().apply {
            this@mapToNet.forEach {
                add(it.mapToNet(courseId))
            }
        }
    }
}