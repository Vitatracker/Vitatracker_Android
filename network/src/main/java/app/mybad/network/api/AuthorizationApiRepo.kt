package app.mybad.network.api

import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import app.mybad.network.models.response.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationApiRepo {

    @Headers("Content-Type: application/json")
    @POST(value = "api/login")
    fun loginUser(@Body authorizationUserLogin: AuthorizationUserLogin): Call<String>

    @Headers("Content-Type: application/json")
    @POST(value = "api/users")
    fun registrationUser(@Body authorizationUserRegistration: AuthorizationUserRegistration): Call<UserModel>

}