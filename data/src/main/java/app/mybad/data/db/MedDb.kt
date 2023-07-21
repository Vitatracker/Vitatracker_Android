package app.mybad.data.db

import app.mybad.data.db.dao.CourseDao
import app.mybad.data.db.dao.NotificationSettingsDao
import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.db.dao.UsageDao
import app.mybad.data.db.dao.UserDao

interface MedDb {
    fun getUserDao(): UserDao
    fun getRemedyDao(): RemedyDao
    fun getCourseDao(): CourseDao
    fun getUsageDao(): UsageDao
    fun getNotificationSettingsDao(): NotificationSettingsDao
}
