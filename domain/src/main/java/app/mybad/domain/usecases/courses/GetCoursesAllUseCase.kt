package app.mybad.domain.usecases.courses

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repos.CoursesNetworkRepo
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.utils.ApiResult
import app.mybad.domain.utils.ERROR_LOCAL
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCoursesAllUseCase @Inject constructor(
    private val repository: CoursesNetworkRepo,
    private val localRepository: CoursesRepo,
) {

    suspend operator fun invoke(userId: Long = AuthToken.userId) = flow {
        emit(repository.getAll())
        emit(ApiResult.ApiSuccess(data = localRepository.getAll(userId)))
    }.catch { ApiResult.ApiError(ERROR_LOCAL, "Error: load courses") }

}
