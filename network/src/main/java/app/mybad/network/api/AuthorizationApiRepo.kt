package app.mybad.network.api

import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import app.mybad.network.models.response.Authorization
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationApiRepo {

    @Headers("Content-Type: application/json")
    @POST(value = "api/login")
    fun loginUser(@Body authorizationUserLogin: AuthorizationUserLogin): Call<Authorization>

    @Headers("Content-Type: application/json")
    @POST(value = "api/users")
    fun registrationUser(@Body authorizationUserRegistration: AuthorizationUserRegistration): Call<Authorization>
}
