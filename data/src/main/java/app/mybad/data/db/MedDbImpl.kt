package app.mybad.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.mybad.data.db.dao.MedDao
import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.db.entity.UsageCommonDataModel
import app.mybad.data.db.entity.Users

@Database(
    entities = [
        Users::class,
        MedDataModel::class,
        CourseDataModel::class,
        UsageCommonDataModel::class
    ], version = 1,
    exportSchema = false,
)
// @TypeConverters(Converters::class)
abstract class MedDbImpl : RoomDatabase(), MedDb {
    abstract override fun getMedDao(): MedDao
}
