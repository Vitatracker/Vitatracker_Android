package app.mybad.network.api

import app.mybad.network.models.response.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface SettingsApiRepo {

    @GET("api/Users/")
    @Headers("Content-Type: application/json")
    fun getUserModel(): Call<UserModel>

    @POST("api/Users/")
    @Headers("Content-Type: application/json")
    fun postUserModel(@Body userModel: UserModel): Call<UserModel>

    @DELETE("api/Users/")
    @Headers("Content-Type: application/json")
    fun deleteUserModel(): Call<UserModel>

    @POST("api/Users/")
    @Headers("Content-Type: application/json")
    fun putUserModel(@Body userModel: UserModel): Call<UserModel>

}