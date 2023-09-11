package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.CourseContract
import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.models.CourseWithParamsModel
import app.mybad.data.db.models.RemedyContract
import app.mybad.utils.currentDateTimeInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query(
        "select B.*, C.${RemedyContract.Columns.NAME}, C.${
            RemedyContract.Columns.DESCRIPTION
        }, C.${
            RemedyContract.Columns.TYPE
        }, C.${
            RemedyContract.Columns.ICON
        }, C.${
            RemedyContract.Columns.COLOR
        }, C.${
            RemedyContract.Columns.DOSE
        }, C.${
            RemedyContract.Columns.BEFORE_FOOD
        }, C.${
            RemedyContract.Columns.MEASURE_UNIT
        }, C.${
            RemedyContract.Columns.PHOTO
        } from ${
            CourseContract.TABLE_NAME
        } B LEFT JOIN ${RemedyContract.TABLE_NAME} C ON B.${CourseContract.Columns.REMEDY_ID} = C.${
            RemedyContract.Columns.ID
        } where B.${CourseContract.Columns.USER_ID} = :userId and B.${CourseContract.Columns.DELETED_DATE} = 0 and B.${
            CourseContract.Columns.IS_FINISHED
        } = 0 and B.${CourseContract.Columns.NOT_USED} = 0 order by B.${CourseContract.Columns.START_DATE}, C.${RemedyContract.Columns.NAME}"
    )
    fun getCoursesWithParams(userId: Long): Flow<List<CourseWithParamsModel>>

    @Query(
        "select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.DELETED_DATE} = 0 and ${
            CourseContract.Columns.USER_ID
        } = :userId"
    )
    fun getCourses(userId: Long): Flow<List<CourseModel>>

    @Query(
        "select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.DELETED_DATE} = 0 and ${
            CourseContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getCoursesByUserId(userId: Long): List<CourseModel>

    @Query(
        "select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.DELETED_DATE} = 0 and ${
            CourseContract.Columns.REMEDY_ID
        } = :remedyId"
    )
    suspend fun getCoursesByRemedyId(remedyId: Long): List<CourseModel>

    //--------------------------------------------------
    // тут и помеченные на удаление, мы берем по id
    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.ID} = :courseId limit 1")
    suspend fun getCourseById(courseId: Long): CourseModel

    @Query("select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.IDN} = :courseIdn limit 1")
    suspend fun getCourseByIdn(courseIdn: Long): CourseModel

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(courses: List<CourseModel>)

    //--------------------------------------------------
    @Query(
        "UPDATE ${CourseContract.TABLE_NAME} SET ${
            CourseContract.Columns.DELETED_DATE
        } = :date WHERE ${CourseContract.Columns.ID} = :courseId"
    )
    suspend fun markDeletionCourseById(courseId: Long, date: Long = currentDateTimeInSecond())

    //--------------------------------------------------
    // тут удаление физически, т.е. то, что было удалено через сервер
    @Query("delete from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.ID} = :courseId")
    suspend fun deleteCoursesById(courseId: Long)

    @Query("delete from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.USER_ID} = :userId")
    suspend fun deleteCoursesByUserId(userId: Long)

    @Delete
    suspend fun deleteCourse(courses: List<CourseModel>)

    //--------------------------------------------------
    @Query(
        "select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.DELETED_DATE} > 0 and ${
            CourseContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getCoursesDeletedByUserId(userId: Long): List<CourseModel>

    @Query(
        "select * from ${CourseContract.TABLE_NAME} where ${CourseContract.Columns.DELETED_DATE} = 0 and ${
            CourseContract.Columns.UPDATED_NETWORK_DATE
        } = 0 and ${CourseContract.Columns.USER_ID} = :userId"
    )
    suspend fun getCoursesNotUpdateByUserId(userId: Long): List<CourseModel>

}
