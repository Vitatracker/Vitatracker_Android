package app.mybad.data.db

import app.mybad.data.db.dao.CourseDao
import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.db.dao.UsageDao
import app.mybad.data.db.dao.UserDao

interface MedDb {
    fun getRemedyDao(): RemedyDao
    fun getUserDao(): UserDao
    fun getUsageDao(): UsageDao
    fun getCourseDao(): CourseDao
}
