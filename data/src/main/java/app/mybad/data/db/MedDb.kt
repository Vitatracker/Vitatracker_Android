package app.mybad.data.db

import app.mybad.data.db.dao.MedDao

interface MedDb {
    fun getMedDao(): MedDao
}
