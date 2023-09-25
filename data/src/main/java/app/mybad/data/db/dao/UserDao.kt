package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.mybad.data.db.models.UserContract
import app.mybad.data.db.models.UserModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.utils.currentDateTimeUTCInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query(
        "select ${UserContract.Columns.IS_DARK_THEME} from ${UserContract.TABLE_NAME} where ${
            UserContract.Columns.ID
        } = :userId limit 1"
    )
    fun isDarkTheme(userId: Long): Flow<Long?>

    @Query("SELECT COUNT(${UserContract.Columns.ID}) FROM ${UserContract.TABLE_NAME}")
    suspend fun getNumberOfUsers(): Long

    @Query("select * from ${UserContract.TABLE_NAME} where ${UserContract.Columns.ID} = :userId limit 1")
    suspend fun getUser(userId: Long): UserModel

    @Query("select * from ${UserContract.TABLE_NAME} ORDER BY ${UserContract.Columns.UPDATED_DATE} DESC limit 1")
    suspend fun getUserLastEntrance(): UserModel?

    @Query("select * from ${UserContract.TABLE_NAME} where ${UserContract.Columns.EMAIL} like :email limit 1")
    suspend fun getUserByEmail(email: String): UserModel?

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserModel): Long

    @Update
    suspend fun updateUser(user: UserModel)

    //--------------------------------------------------
    @Query(
        "UPDATE ${UserContract.TABLE_NAME} SET ${
            UserContract.Columns.DELETED_DATE
        } = :date WHERE ${UserContract.Columns.ID} = :userId"
    )
    suspend fun markDeletionUserById(userId: Long, date: Long = currentDateTimeUTCInSecond())

    //--------------------------------------------------
    // тут удаление физически, т.е. то, что было удалено через сервер
    @Query("delete from ${UserContract.TABLE_NAME} where ${UserContract.Columns.ID} = :userId")
    suspend fun deleteUserById(userId: Long)

    //--------------------------------------------------
    @Query(
        "UPDATE ${UserContract.TABLE_NAME} SET ${
            UserContract.Columns.SYNCHRONIZE_DATE
        } = :date, ${
            UserContract.Columns.UPDATED_DATE
        } = :date WHERE ${UserContract.Columns.ID} = :userId"
    )
    suspend fun synchronization(userId: Long, date: Long = currentDateTimeUTCInSecond())

    @Query("SELECT *  from ${UserContract.TABLE_NAME} where ${UserContract.Columns.ID} = :userId limit 1")
    fun getUserPersonal(userId: Long): PersonalDataModel?

}
