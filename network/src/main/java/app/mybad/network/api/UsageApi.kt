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
    suspend fun getUsages(): List<UsageNetworkModel>

    @GET("api/courses/{id}/usages")
    suspend fun getUsagesByCourseId(@Path("id") courseId: Long): List<UsageNetworkModel>

    @GET("api/usages/{id}")
    suspend fun getUsage(@Path("id") usageId: Long): UsageNetworkModel

    @POST("api/usages")
    suspend fun addUsage(@Body usage: UsageNetworkModel): UsageNetworkModel

    @PUT("api/usages")
    suspend fun updateUsage(@Body usage: UsageNetworkModel): UsageNetworkModel

    @DELETE("api/usages/{id}")
    suspend fun deleteUsage(@Path("id") usageId: Long): Response<Unit>
}
