package app.mybad.network.api

import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import app.mybad.network.models.response.Authorization
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationApi {

    @Headers("Content-Type: application/json")
    @POST(value = "api/v1/auth/authenticate")
    fun loginUser(@Body authorizationUserLogin: AuthorizationUserLogin): Call<Authorization>

    @Headers("Content-Type: application/json")
    @POST(value = "api/v1/auth/register")
    fun registrationUser(@Body authorizationUserRegistration: AuthorizationUserRegistration): Call<Authorization>
}
