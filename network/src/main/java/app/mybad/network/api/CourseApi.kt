package app.mybad.network.api

import app.mybad.network.models.response.CourseNetworkModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CourseApi {

    @GET("api/courses")
    suspend fun getCourses(): List<CourseNetworkModel>

    @GET("api/remedies/{id}/courses")
    suspend fun getCoursesByRemedyId(@Path("id") remedyId: Long): List<CourseNetworkModel>

    @GET("api/courses/{id}")
    suspend fun getCourse(@Path("id") courseId: Long): CourseNetworkModel

    @POST("api/courses")
    suspend fun addCourse(@Body course: CourseNetworkModel): CourseNetworkModel

    @PUT("api/courses")
    suspend fun updateCourse(@Body course: CourseNetworkModel): CourseNetworkModel

    @DELETE("api/courses/{id}")
    suspend fun deleteCourse(@Path("id") courseId: Long): Response<Unit>

}
