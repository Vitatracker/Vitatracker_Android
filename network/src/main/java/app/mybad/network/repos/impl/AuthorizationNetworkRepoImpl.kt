package app.mybad.network.repos.impl

import android.util.Log
import app.mybad.domain.models.authorization.AuthorizationUserLoginDomainModel
import app.mybad.domain.models.authorization.AuthorizationUserRegistrationDomainModel
import app.mybad.domain.repos.AuthorizationNetworkRepository
import app.mybad.domain.utils.ApiResult
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.mapToNet
import app.mybad.network.utils.ApiHandler.handleApi
import retrofit2.Call
import javax.inject.Inject

class AuthorizationNetworkRepoImpl @Inject constructor(
    private val authorizationApiRepo: AuthorizationApi
) : AuthorizationNetworkRepository {

    override suspend fun loginUser(authorizationUserLogin: AuthorizationUserLoginDomainModel): ApiResult =
        execute { authorizationApiRepo.loginUser(authorizationUserLogin.mapToNet()) }

    override suspend fun registrationUser(
        authorizationUserRegistration: AuthorizationUserRegistrationDomainModel
    ): ApiResult = execute {
        Log.w("VTTAG", "[AuthorizationNetworkRepoImpl:registrationUser]: in")
        authorizationApiRepo.registrationUser(authorizationUserRegistration.mapToNet())
    }

    private suspend fun execute(request: () -> Call<*>): ApiResult {
        Log.w("VTTAG", "[AuthorizationNetworkRepoImpl:execute]: in")
        return when (val response = handleApi { request.invoke().execute() }) {
            is ApiResult.ApiSuccess -> {
                Log.w(
                    "VTTAG",
                    "[AuthorizationNetworkRepoImpl:execute]: ApiSuccess - ${response.data}"
                )
                ApiResult.ApiSuccess(data = response.data)
            }

            is ApiResult.ApiError -> {
                Log.w(
                    "VTTAG",
                    "[AuthorizationNetworkRepoImpl:execute]: ApiError - ${response.code}"
                )
                ApiResult.ApiError(
                    code = response.code,
                    message = response.message
                )
            }

            is ApiResult.ApiException -> {
                Log.w(
                    "VTTAG",
                    "[AuthorizationNetworkRepoImpl:execute]: ApiException - ${response.e}"
                )
                ApiResult.ApiException(e = response.e)
            }
        }
    }
}
