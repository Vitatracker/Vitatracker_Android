package app.mybad.network.api

import app.mybad.network.models.response.UsageNetworkModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsageApi {

    @GET("api/usages")
    fun getUsages(): List<UsageNetworkModel>

    @GET("api/usages/{id}")
    fun getUsage(@Path("id") usageId: Long): UsageNetworkModel

    @POST("api/usages")
    fun addUsage(@Body usage: UsageNetworkModel): Response<Unit>

    @PUT("api/usages")
    fun updateUsage(@Body usage: UsageNetworkModel): Response<Unit>

    @DELETE("api/usages/{id}")
    fun deleteUsage(@Path("id") usageId: Long): Response<Unit>
}
