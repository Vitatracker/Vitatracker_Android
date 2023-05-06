package app.mybad.domain.repos

import app.mybad.domain.utils.ApiResult

interface AuthorizationRepo {

    suspend fun loginWithFacebook()

    suspend fun loginWithGoogle()

    suspend fun loginWithEmail(login: String, password: String): ApiResult

    suspend fun registrationUser(login: String, password: String, userName: String): ApiResult

}
