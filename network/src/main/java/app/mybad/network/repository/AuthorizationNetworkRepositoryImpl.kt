package app.mybad.network.repository

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.AuthorizationDomainModel
import app.mybad.domain.models.SetNewPasswordDomainModel
import app.mybad.domain.models.VerificationCodeDomainModel
import app.mybad.domain.repository.network.AuthorizationGoogleRepository
import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.request.UserChangePasswordRequestModel
import app.mybad.network.models.request.UserLoginRequestModel
import app.mybad.network.models.request.UserRegistrationRequestModel
import app.mybad.network.models.request.UserSetNewPasswordRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.openid.appauth.TokenRequest
import javax.inject.Inject
import javax.inject.Named

class AuthorizationNetworkRepositoryImpl @Inject constructor(
    private val authorizationApi: AuthorizationApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : AuthorizationNetworkRepository {

    override suspend fun loginWithFacebook() = withContext(dispatcher) {
        runCatching {
            TODO("Not yet implemented")
        }
    }

    override suspend fun loginWithGoogle(tokenRequest: TokenRequest) = withContext(dispatcher) {
        runCatching {
            "google@gmail.com" to tokenRequest.mapToDomain()
        }
    }

    override suspend fun loginWithEmail(
        login: String,
        password: String
    ) = withContext(dispatcher) {
        runCatching {
            authorizationApi.loginUser(
                UserLoginRequestModel(
                    email = login,
                    password = password,
                )
            ).mapToDomain()
        }
    }

    override suspend fun registrationUser(
        login: String,
        password: String,
        userName: String
    ) = withContext(dispatcher) {
        runCatching {
            Log.w("VTTAG", "AuthorizationNetworkRepository::registrationUser: in")
            val response = authorizationApi.registrationUser(
                UserRegistrationRequestModel(
                    email = login,
                    password = password,
                    name = userName
                )
            )
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.mapToDomain()
            } else {
                // response.message() - это не работает и строка пустая
                val message = response.message().ifBlank {
                    val pattern = """^.*message":"(.+?)",".*$""".toRegex()
                    response.errorBody()?.string()?.let { message ->
                        pattern.find(message)?.let { it.groupValues[1] }
                    } ?: "Error: unknown network error!"
                }
                Log.w(
                    "VTTAG",
                    "AuthorizationNetworkRepository::registrationUser: message=$message"
                )
                AuthorizationDomainModel(message = message)
            }
        }
    }

    override suspend fun refreshToken() = withContext(dispatcher) {
        runCatching {
            Log.w(
                "VTTAG",
                "AuthorizationNetworkRepositoryImpl::refreshToken  userId=${AuthToken.userId}"
            )
            AuthToken.token = AuthToken.tokenRefresh
            authorizationApi.refreshToken().mapToDomain()
        }
    }

    override suspend fun restorePassword(email: String) = withContext(dispatcher) {
        runCatching {
            authorizationApi.restorePassword(email).mapToDomain()
        }
    }

    override suspend fun sendVerificationCode(code: Int): Result<VerificationCodeDomainModel> =
        withContext(dispatcher) {
            runCatching {
                authorizationApi.sendVerificationCode(code).mapToDomain()
            }
        }

    override suspend fun changeUserPassword(
        oldPassword: String,
        newPassword: String
    ) = withContext(dispatcher) {
        runCatching {
            authorizationApi.changeUserPassword(
                UserChangePasswordRequestModel(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
            ).isSuccessful
        }
    }

    override suspend fun setNewUserPassword(
        token: String,
        password: String,
        email: String
    ): Result<SetNewPasswordDomainModel> = withContext(dispatcher) {
        runCatching {
            authorizationApi.setNewUserPassword(
                UserSetNewPasswordRequestModel(
                    token = token,
                    password = password,
                    email = email
                )
            ).mapToDomain()
        }
    }
}
