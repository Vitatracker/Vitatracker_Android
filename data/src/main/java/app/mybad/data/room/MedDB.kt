package app.mybad.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.usages.UsageCommonDataModel

@Database(entities = [MedDataModel::class, CourseDataModel::class, UsageCommonDataModel::class], version = 1)
//@TypeConverters(Converters::class)
abstract class MedDB : RoomDatabase() {
    abstract fun dao() : MedDAO
}