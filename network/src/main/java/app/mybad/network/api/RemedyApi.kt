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
    suspend fun getRemedies(): List<RemedyNetworkModel>

    @GET("api/remedies/{id}")
    suspend fun getRemedy(@Path("id") remedyId: Long): RemedyNetworkModel

    @POST("api/remedies")
    suspend fun addRemedy(@Body remedy: RemedyNetworkModel): RemedyNetworkModel

    @PUT("api/remedies")
    suspend fun updateRemedy(@Body remedy: RemedyNetworkModel): RemedyNetworkModel

    @DELETE("api/remedies/{id}")
    suspend fun deleteRemedy(@Path("id") remedyId: Long): Response<Unit>

}
