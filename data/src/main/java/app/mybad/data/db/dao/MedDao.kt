package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.db.entity.UsageCommonDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDao {

    @Query("select * from meds where id=(:medId) limit 1")
    fun getMedById(medId: Long): MedDataModel

    @Query("select * from meds")
    fun getAllMeds(): List<MedDataModel>

    @Query("select * from meds where userId =:userId")
    fun getAllMedsFlow(userId: Long): Flow<List<MedDataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMed(med: MedDataModel): Long?

    @Query("delete from meds where id=(:medId)")
    fun deleteMed(medId: Long)

    @Query("select * from courses where id=(:courseId) limit 1")
    fun getCourseById(courseId: Long): CourseDataModel

    @Query("select * from courses where userId = :userId")
    fun getAllCourses(userId: Long): List<CourseDataModel>

    @Query("select * from courses where userId = :userId")
    fun getAllCoursesFlow(userId: Long): Flow<List<CourseDataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourse(course: CourseDataModel): Long?

    @Query("delete from courses where id=(:courseId)")
    fun deleteCourse(courseId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsages(usages: List<UsageCommonDataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateSingleUsage(usage: UsageCommonDataModel)

    @Query("select * from usages_common where medId=(:medId)")
    fun getUsagesById(medId: Long): List<UsageCommonDataModel>

    @Query("select * from usages_common where medId=(:medId) and useTime between (:startTime) and (:endTime)")
    fun getUsagesByIntervalByMed(medId: Long, startTime: Long, endTime: Long): List<UsageCommonDataModel>

    @Query("select * from usages_common where medId=(:medId) and useTime >= (:time)")
    fun getUsagesAfter(medId: Long, time: Long): List<UsageCommonDataModel>

    @Query("delete from usages_common where medId=(:medId)")
    fun deleteUsagesById(medId: Long)

    @Query("delete from usages_common where medId=(:medId) and useTime between (:startTime) and (:endTime)")
    fun deleteUsagesByInterval(medId: Long, startTime: Long, endTime: Long)

    @Query("delete from usages_common where medId=(:medId) and useTime >= (:time)")
    fun deleteUsagesAfter(medId: Long, time: Long)

    @Query("select * from usages_common where userId =:userId")
    fun getAllCommonUsagesFlow(userId: Long): Flow<List<UsageCommonDataModel>>

    @Query("SELECT * FROM usages_common WHERE useTime BETWEEN (:startTime) AND (:endTime)")
    fun getUsagesByInterval(startTime: Long, endTime: Long): List<UsageCommonDataModel>

    @Query("SELECT * FROM meds WHERE id IN (:listId) ")
    fun getMedByList(listId: List<Long>): List<MedDataModel>
}
