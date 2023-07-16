package app.mybad.network.api

import app.mybad.network.models.request.UserLoginRequestModel
import app.mybad.network.models.request.UserRegistrationRequestModel
import app.mybad.network.models.response.AuthorizationNetworkModel
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApi {

    @POST(value = "api/v1/auth/authenticate")
    suspend fun loginUser(@Body loginModel: UserLoginRequestModel): AuthorizationNetworkModel

    @POST(value = "api/v1/auth/register")
    suspend fun registrationUser(@Body registrationModel: UserRegistrationRequestModel): AuthorizationNetworkModel
}
