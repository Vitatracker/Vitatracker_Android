package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.RemedyContract
import app.mybad.data.db.models.RemedyModel
import app.mybad.utils.currentDateTimeInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface RemedyDao {

    @Query(
        "select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.DELETED_DATE} = 0 and ${
            RemedyContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getRemediesByUserId(userId: Long): List<RemedyModel>

    @Query(
        "select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.DELETED_DATE} = 0 and ${
            RemedyContract.Columns.USER_ID
        } = :userId"
    )
    fun getRemedies(userId: Long): Flow<List<RemedyModel>>

    //--------------------------------------------------
    @Query("select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.ID} = :remedyId limit 1")
    suspend fun getRemedyById(remedyId: Long): RemedyModel

    @Query("select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.IDN} = :remedyIdn limit 1")
    suspend fun getRemedyByIdn(remedyIdn: Long): RemedyModel

    @Query("SELECT * FROM ${RemedyContract.TABLE_NAME} WHERE ${RemedyContract.Columns.ID} IN (:remedyIdList)")
    fun getRemediesByIds(remedyIdList: List<Long>): Flow<List<RemedyModel>>

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemedy(remedy: RemedyModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemedy(remedies: List<RemedyModel>)

    //--------------------------------------------------
    @Query(
        "UPDATE ${RemedyContract.TABLE_NAME} SET ${
            RemedyContract.Columns.DELETED_DATE
        } = :date WHERE ${RemedyContract.Columns.ID} = :remedyId"
    )
    suspend fun markDeletionRemedyById(remedyId: Long, date: Long = currentDateTimeInSecond())

    //--------------------------------------------------
    // тут удаление физически, т.е. то, что было удалено через сервер
    @Query("delete from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.USER_ID} = :remedyId")
    suspend fun deleteRemedyById(remedyId: Long)

    @Query("delete from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.USER_ID} = :userId")
    suspend fun deleteRemediesByUserId(userId: Long)

    @Delete
    suspend fun deleteRemedies(remedies: List<RemedyModel>)

    //--------------------------------------------------
    @Query(
        "select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.DELETED_DATE} = 0 and ${
            RemedyContract.Columns.UPDATED_NETWORK_DATE
        } = 0 and ${RemedyContract.Columns.USER_ID} = :userId"
    )
    suspend fun getRemedyNotUpdateByUserId(userId: Long): List<RemedyModel>

    @Query(
        "select * from ${RemedyContract.TABLE_NAME} where ${RemedyContract.Columns.DELETED_DATE} > 0 and ${
            RemedyContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getRemedyDeletedByUserId(userId: Long): List<RemedyModel>

}
