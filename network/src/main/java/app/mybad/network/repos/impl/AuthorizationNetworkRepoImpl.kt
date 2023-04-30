package app.mybad.network.repos.impl

import app.mybad.network.api.AuthorizationApiRepo
import app.mybad.network.models.AuthorizationUserNetwork
import app.mybad.network.repos.repo.AuthorizationNetworkRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationNetworkRepoImpl @Inject constructor(
    private val authorizationApiRepo: AuthorizationApiRepo
) : AuthorizationNetworkRepo {

    override suspend fun loginUser(authorizationUserNetwork: AuthorizationUserNetwork): String =
        authorizationApiRepo.loginUser(authorizationUserNetwork = authorizationUserNetwork)

    override suspend fun registrationUser(authorizationUserNetwork: AuthorizationUserNetwork): String =
        authorizationApiRepo.registrationUser(authorizationUserNetwork = authorizationUserNetwork)

}