package app.mybad.network.api

import app.mybad.network.models.response.RemedyNetworkModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RemedyApi {
    @GET("api/remedies")
    fun getRemedies(): List<RemedyNetworkModel>

    @GET("api/remedies/{id}")
    fun getRemedy(@Path("id") remedyId: Long): RemedyNetworkModel

    @POST("api/remedies")
    fun addRemedy(@Body remedy: RemedyNetworkModel): Response<Unit>

    @PUT("api/remedies")
    fun updateRemedy(@Body remedy: RemedyNetworkModel): Response<Unit>

    @DELETE("api/remedies/{id}")
    fun deleteRemedy(@Path("id") remedyId: Long): Response<Unit>

}
