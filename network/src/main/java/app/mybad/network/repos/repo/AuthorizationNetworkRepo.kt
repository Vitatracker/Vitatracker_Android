package app.mybad.network.repos.repo

import app.mybad.network.models.AuthorizationUserNetwork
import app.mybad.network.models.response.AuthorizationToken

interface AuthorizationNetworkRepo {

    suspend fun loginUser(authorizationUserNetwork: AuthorizationUserNetwork): AuthorizationToken

}