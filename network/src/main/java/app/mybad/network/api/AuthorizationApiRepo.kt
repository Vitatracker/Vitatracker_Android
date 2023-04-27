package app.mybad.network.api

import app.mybad.network.models.AuthorizationUserNetwork
import app.mybad.network.models.response.AuthorizationToken
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationApiRepo {

    @Headers("Content-Type: application/json")
    @POST(value = "api/login")
    suspend fun loginUser(@Body authorizationUserNetwork: AuthorizationUserNetwork): AuthorizationToken

    @Headers("Content-Type: application/json")
    @POST(value = "api/login")
    fun registrationUser(@Body authorizationUserNetwork: AuthorizationUserNetwork): AuthorizationToken

}