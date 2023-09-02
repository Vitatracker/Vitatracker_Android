package app.mybad.domain.repository.network

import app.mybad.domain.models.AuthorizationDomainModel
import app.mybad.domain.models.PasswordDomainModel

interface AuthorizationNetworkRepository {

    suspend fun loginWithFacebook(): Result<AuthorizationDomainModel>

    suspend fun loginWithGoogle(): Result<AuthorizationDomainModel>

    suspend fun loginWithEmail(
        login: String,
        password: String
    ): Result<AuthorizationDomainModel>

    suspend fun registrationUser(
        login: String,
        password: String,
        userName: String
    ): Result<AuthorizationDomainModel>

    suspend fun refreshToken(): Result<AuthorizationDomainModel>

    suspend fun restorePassword(email: String): Result<PasswordDomainModel>
}
