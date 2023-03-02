package app.mybad.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.usages.UsageCommonDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDAO {

    @Query("select * from meds where id=(:medId) limit 1")
    fun getMedById(medId: Long) : MedDataModel
    @Query("select * from meds")
    fun getAllMeds() : List<MedDataModel>
    @Query("select * from meds")
    fun getAllMedsFlow() : Flow<List<MedDataModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMed(med: MedDataModel)
    @Query("delete from meds where id=(:medId)")
    fun deleteMed(medId: Long)

    @Query("select * from courses where id=(:courseId) limit 1")
    fun getCourseById(courseId: Long) : CourseDataModel
    @Query("select * from courses")
    fun getAllCourses() : List<CourseDataModel>
    @Query("select * from courses")
    fun getAllCoursesFlow() : Flow<List<CourseDataModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourse(course: CourseDataModel)
    @Query("delete from courses where id=(:courseId)")
    fun deleteCourse(courseId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsages(usages: List<UsageCommonDataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateSingleUsage(usage: UsageCommonDataModel)
    @Query("select * from usages_common where medId=(:medId)")
    fun getUsagesById(medId: Long) : List<UsageCommonDataModel>
    @Query("select * from usages_common where medId=(:medId) and useTime between (:startTime) and (:endTime)")
    fun getUsagesByIntervalByMed(medId: Long, startTime: Long, endTime: Long) : List<UsageCommonDataModel>
    @Query("delete from usages_common where medId=(:medId)")
    fun deleteUsagesById(medId: Long)
    @Query("delete from usages_common where medId=(:medId) and useTime between (:startTime) and (:endTime)")
    fun deleteUsagesByInterval(medId: Long, startTime: Long, endTime: Long)
    @Query("select * from usages_common")
    fun getAllCommonUsagesFlow() : Flow<List<UsageCommonDataModel>>
    @Query("SELECT * FROM usages_common WHERE useTime between (:startTime) AND (:endTime)")
    fun getUsagesByInterval(startTime: Long, endTime: Long) : List<UsageCommonDataModel>
}