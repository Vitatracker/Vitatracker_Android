package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.CourseContract
import app.mybad.data.db.models.CourseModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.USER_ID} = :userId")
    fun getCourses(userId: Long): Flow<List<CourseModel>>

    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.USER_ID} = :userId")
    suspend fun getCoursesByUserId(userId: Long): List<CourseModel>

    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.REMEDY_ID} = :remedyId")
    suspend fun getCoursesByRemedyId(remedyId: Long): List<CourseModel>

    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.ID} = :courseId limit 1")
    suspend fun getCourseById(courseId: Long): CourseModel

    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.IDN} = :courseIdn limit 1")
    suspend fun getCourseByIdn(courseIdn: Long): CourseModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseModel): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(courses: List<CourseModel>)

    @Query("delete from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.ID} = :courseId")
    suspend fun deleteCourseById(courseId: Long)

    @Delete
    suspend fun deleteCourse(courses: List<CourseModel>)

    @Query(
        "select * from ${CourseContract.TABLE_NAME} where ${
            CourseContract.Columns.USER_ID
        } = :userId and ${CourseContract.Columns.UPDATED_NETWORK_DATE} = 0"
    )
    suspend fun getCoursesNotUpdateByUserId(userId: Long): List<CourseModel>
}
