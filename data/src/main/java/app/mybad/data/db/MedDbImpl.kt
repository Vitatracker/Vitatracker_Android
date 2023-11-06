package app.mybad.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.mybad.data.db.dao.CourseDao
import app.mybad.data.db.dao.NotificationDao
import app.mybad.data.db.dao.PatternUsageDao
import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.db.dao.UsageDao
import app.mybad.data.db.dao.UserDao
import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.models.NotificationModel
import app.mybad.data.db.models.PatternUsageModel
import app.mybad.data.db.models.RemedyModel
import app.mybad.data.db.models.UsageModel
import app.mybad.data.db.models.UserModel

@Database(
    entities = [
        UserModel::class,
        RemedyModel::class,
        CourseModel::class,
        UsageModel::class,
        PatternUsageModel::class,
        NotificationModel::class,
    ],
    version = MedDbImpl.DB_VERSION,
    exportSchema = false,
)
abstract class MedDbImpl : RoomDatabase(), MedDb {
    abstract override fun getUserDao(): UserDao
    abstract override fun getRemedyDao(): RemedyDao
    abstract override fun getCourseDao(): CourseDao
    abstract override fun getUsageDao(): UsageDao
    abstract override fun getPatternUsageDao(): PatternUsageDao
    abstract override fun getNotificationDao(): NotificationDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "meds.db"
    }
}
