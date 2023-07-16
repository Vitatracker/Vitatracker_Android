package app.mybad.network.repository

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.request.UserLoginRequestModel
import app.mybad.network.models.request.UserRegistrationRequestModel
import javax.inject.Inject

class AuthorizationNetworkRepositoryImpl @Inject constructor(
    private val authorizationApi: AuthorizationApi
) : AuthorizationNetworkRepository {

    override suspend fun loginWithFacebook() = Result.runCatching {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithGoogle() = Result.runCatching {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmail(
        login: String,
        password: String
    ) = Result.runCatching {
        authorizationApi.loginUser(
            UserLoginRequestModel(
                email = login,
                password = password,
            )
        ).mapToDomain()
    }

    override suspend fun registrationUser(
        login: String,
        password: String,
        userName: String
    ) = Result.runCatching {
        authorizationApi.registrationUser(
            UserRegistrationRequestModel(
                email = login,
                password = password,
                name = userName
            )
        ).mapToDomain()
    }

}
