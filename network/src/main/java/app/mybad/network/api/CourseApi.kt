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
    fun getCourses(): List<CourseNetworkModel>

    @GET("api/courses/{id}")
    fun getCourse(@Path("id") courseId: Long): CourseNetworkModel

    @POST("api/courses")
    fun addCourse(@Body course: CourseNetworkModel): Response<Unit>

    @PUT("api/courses")
    fun updateCourse(@Body course: CourseNetworkModel): Response<Unit>

    @DELETE("api/courses/{id}")
    fun deleteCourse(@Path("id") courseId: Long): Response<Unit>

}
