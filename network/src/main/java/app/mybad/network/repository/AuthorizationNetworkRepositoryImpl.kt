package app.mybad.network.repository

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.request.UserLoginRequestModel
import app.mybad.network.models.request.UserRegistrationRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class AuthorizationNetworkRepositoryImpl @Inject constructor(
    private val authorizationApi: AuthorizationApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : AuthorizationNetworkRepository {

    override suspend fun loginWithFacebook() = withContext(dispatcher)
    {
        Result.runCatching {
            TODO("Not yet implemented")
        }
    }

    override suspend fun loginWithGoogle() = withContext(dispatcher) {
        Result.runCatching {
            TODO("Not yet implemented")
        }
    }

    override suspend fun loginWithEmail(
        login: String,
        password: String
    ) = withContext(dispatcher) {
        Result.runCatching {
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
        Result.runCatching {
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
        Result.runCatching {
            AuthToken.token = AuthToken.tokenRefresh
            authorizationApi.refreshToken().mapToDomain()
        }
    }

}
