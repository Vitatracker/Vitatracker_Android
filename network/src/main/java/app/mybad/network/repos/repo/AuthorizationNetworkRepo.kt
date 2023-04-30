package app.mybad.network.repos.repo

import app.mybad.network.models.AuthorizationUserNetwork

interface AuthorizationNetworkRepo {

    suspend fun loginUser(authorizationUserNetwork: AuthorizationUserNetwork): String

    suspend fun registrationUser(authorizationUserNetwork: AuthorizationUserNetwork): String

}