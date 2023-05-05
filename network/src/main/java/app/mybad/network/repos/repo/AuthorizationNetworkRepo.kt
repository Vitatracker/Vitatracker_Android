package app.mybad.network.repos.repo

import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import app.mybad.network.utils.ApiResult

interface AuthorizationNetworkRepo {

    suspend fun loginUser(authorizationUserLogin: AuthorizationUserLogin): ApiResult

    suspend fun registrationUser(authorizationUserRegistration: AuthorizationUserRegistration): ApiResult

}