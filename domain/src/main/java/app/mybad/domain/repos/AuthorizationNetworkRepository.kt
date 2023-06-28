package app.mybad.domain.repos

import app.mybad.domain.models.authorization.AuthorizationUserLoginDomainModel
import app.mybad.domain.models.authorization.AuthorizationUserRegistrationDomainModel
import app.mybad.domain.utils.ApiResult

interface AuthorizationNetworkRepository {

    suspend fun loginUser(authorizationUserLogin: AuthorizationUserLoginDomainModel): ApiResult

    suspend fun registrationUser(authorizationUserRegistration: AuthorizationUserRegistrationDomainModel): ApiResult

}
