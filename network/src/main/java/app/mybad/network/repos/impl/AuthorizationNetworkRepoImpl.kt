package app.mybad.network.repos.impl

import app.mybad.domain.utils.ApiResult
import app.mybad.network.utils.ApiHandler.handleApi
import app.mybad.network.api.AuthorizationApiRepo
import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import app.mybad.network.repos.repo.AuthorizationNetworkRepo
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationNetworkRepoImpl @Inject constructor(
    private val authorizationApiRepo: AuthorizationApiRepo
) : AuthorizationNetworkRepo {

    override suspend fun loginUser(authorizationUserLogin: AuthorizationUserLogin): ApiResult =
        execute { authorizationApiRepo.loginUser(authorizationUserLogin = authorizationUserLogin) }

    override suspend fun registrationUser(authorizationUserRegistration: AuthorizationUserRegistration): ApiResult =
        execute {
            authorizationApiRepo.registrationUser(authorizationUserRegistration = authorizationUserRegistration)
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
}
