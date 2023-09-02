package app.mybad.network.repository

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.request.UserLoginRequestModel
import app.mybad.network.models.request.UserRegistrationRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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

    override suspend fun loginWithGoogle() = withContext(dispatcher) {
        runCatching {
            TODO("Not yet implemented")
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
            authorizationApi.registrationUser(
                UserRegistrationRequestModel(
                    email = login,
                    password = password,
                    name = userName
                )
            ).mapToDomain()
        }
    }

    override suspend fun refreshToken() = withContext(dispatcher) {
        runCatching {
            Log.w("VTTAG", "AuthorizationNetworkRepositoryImpl::refreshToken  userId=${AuthToken.userId}")
            AuthToken.token = AuthToken.tokenRefresh
            authorizationApi.refreshToken().mapToDomain()
        }
    }

    override suspend fun restorePassword(email: String) = withContext(dispatcher) {
        runCatching {
            authorizationApi.restorePassword(email).mapToDomain()
        }
    }
}
