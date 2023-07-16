package app.mybad.network.api

import app.mybad.network.models.response.UserNetworkModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SettingsApi {

    @GET("api/users/{id}")
    fun getUser(@Path("id") id: String): UserNetworkModel

    @POST("api/users/")
    fun addUser(@Body userModel: UserNetworkModel): Response<Unit>

    @PUT("api/users/")
    fun updateUser(@Body userModel: UserNetworkModel): Response<Unit>

    @DELETE("api/users/{id}/delete")
    fun deleteUser(@Path("id") id: String): Response<Unit>
}
