package app.mybad.network.api

import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.network.models.response.Courses
import app.mybad.network.models.response.Remedies
import app.mybad.network.models.response.Usages
import app.mybad.network.models.response.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CoursesApi {
    @GET("api/Users/{id}")
    @Headers("Content-Type: application/json")
    fun getUserModel(@Path("id") id: Long): Call<UserModel>

    @GET("api/Remedies?strategy=haveAttach")
    @Headers("Content-Type: application/json")
    fun getAll(): Call<List<Remedies>>

    @PUT("api/Usages/{id}")
    @Headers("Content-Type: application/json")
    fun updateUsage(@Body usage: Usages, @Path("id") id : Long): Call<Any>

    @PUT("api/Courses")
    @Headers("Content-Type: application/json")
    fun updateCourse(@Body course: Courses): Call<Any>

    @PUT("api/Remedies")
    @Headers("Content-Type: application/json")
    fun updateAll(@Body med: Remedies): Call<Any>

    @POST("api/Courses")
    @Headers("Content-Type: application/json")
    fun addCourse(@Body course: Courses): Call<Any>

    @POST("api/Remedies")
    @Headers("Content-Type: application/json")
    fun addAll(@Body med: Remedies): Call<Any>

    @DELETE("api/Usages/{id}")
    @Headers("Content-Type: application/json")
    fun deleteUsage(@Path("id") usageId: Long): Call<Any>

    @DELETE("api/Courses/{id}")
    @Headers("Content-Type: application/json")
    fun deleteCourse(@Path("id") courseId: Long): Call<Any>

    @DELETE("api/Remedies/{id}")
    @Headers("Content-Type: application/json")
    fun deleteMed(@Path("id") medId: Long): Call<Any>
}
