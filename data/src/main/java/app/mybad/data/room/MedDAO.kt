package app.mybad.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.usages.UsagesDataModel

@Dao
interface MedDAO {

    @Query("select * from meds where id=(:medId) limit 1")
    fun getMedById(medId: Long) : MedDataModel
    @Query("select * from meds")
    fun getAllMeds() : List<MedDataModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMed(med: MedDataModel)
    @Query("delete from meds where id=(:medId)")
    fun deleteMed(medId: Long)

    @Query("select * from courses where id=(:courseId) limit 1")
    fun getCourseById(courseId: Long) : CourseDataModel
    @Query("select * from courses")
    fun getAllCourses() : List<CourseDataModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourse(course: CourseDataModel)
    @Query("delete from courses where id=(:courseId)")
    fun deleteCourse(courseId: Long)

    @Query("select * from usages where medId=(:medId) limit 1")
    fun getUsagesByMedId(medId: Long) : UsagesDataModel
    @Query("select * from usages")
    fun getAllUsages() : List<UsagesDataModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsages(usages: UsagesDataModel)
    @Query("delete from usages where medId=(:medId)")
    fun deleteUsagesByMedId(medId: Long)
}