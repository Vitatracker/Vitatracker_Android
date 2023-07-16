package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.RemedyContract
import app.mybad.data.db.models.RemedyModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RemedyDao {

    @Query("select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.USER_ID} = :userId")
    suspend fun getRemediesByUserId(userId: Long): List<RemedyModel>

    @Query("select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.USER_ID} = :userId")
    fun getRemedies(userId: Long): Flow<List<RemedyModel>>

    @Query("select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.ID} = :remedyId limit 1")
    suspend fun getRemedyById(remedyId: Long): RemedyModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRemedy(remedy: RemedyModel): Long?

    @Query("delete from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.ID} = :remedyId")
    suspend fun deleteRemedyById(remedyId: Long)

    @Query("SELECT * FROM ${RemedyContract.TABLE_NAME} WHERE ${RemedyContract.Columns.ID} IN (:remedyIdList)")
    suspend fun getRemediesByIds(remedyIdList: List<Long>): List<RemedyModel>

    @Query(
        "select * from ${RemedyContract.TABLE_NAME} where ${
            RemedyContract.Columns.USER_ID
        } = :userId and ${RemedyContract.Columns.UPDATED_NETWORK_DATE} = 0"
    )
    suspend fun getRemedyNotUpdateByUserId(userId: Long): List<RemedyModel>

}
