package app.mybad.data.repos

import app.mybad.domain.models.authorization.AuthorizationUserLoginDomainModel
import app.mybad.domain.models.authorization.AuthorizationUserRegistrationDomainModel
import app.mybad.domain.repos.AuthorizationNetworkRepository
import app.mybad.domain.repos.AuthorizationRepo
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthorizationRepoImpl @Inject constructor(
    private val authorizationNetworkRepo: AuthorizationNetworkRepository,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : AuthorizationRepo {

    override suspend fun loginWithFacebook() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithGoogle() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmail(
        login: String,
        password: String
    ): ApiResult = withContext(dispatcher) {
        authorizationNetworkRepo.loginUser(
            authorizationUserLogin = AuthorizationUserLoginDomainModel(
                email = login,
                password = password
            )
        )
    }

    override suspend fun registrationUser(
        login: String,
        password: String,
        userName: String
    ): ApiResult = withContext(dispatcher) {
        authorizationNetworkRepo.registrationUser(
            authorizationUserRegistration = AuthorizationUserRegistrationDomainModel(
                email = login,
                password = password,
                name = userName
            )
        )
    }
}
