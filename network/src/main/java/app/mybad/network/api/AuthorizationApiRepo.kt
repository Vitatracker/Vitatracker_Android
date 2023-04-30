package app.mybad.network.api

import app.mybad.network.models.AuthorizationUserNetwork
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationApiRepo {

    @Headers("Content-Type: application/json")
    @POST(value = "api/login")
    suspend fun loginUser(@Body authorizationUserNetwork: AuthorizationUserNetwork): String

    @Headers("Content-Type: application/json")
    @POST(value = "api/users")
    suspend fun registrationUser(@Body authorizationUserNetwork: AuthorizationUserNetwork): String

}