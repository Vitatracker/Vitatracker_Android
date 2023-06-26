package app.mybad.data.db

import app.mybad.data.db.dao.MedDao
import app.mybad.data.db.dao.UsersDao

interface MedDb {
    fun getMedDao(): MedDao
    fun getUsersDao(): UsersDao
}
