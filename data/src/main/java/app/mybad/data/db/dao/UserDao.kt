package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.mybad.data.db.models.UserContract
import app.mybad.data.db.models.UserModel
import app.mybad.utils.currentDateTimeInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("select * from ${UserContract.TABLE_NAME} where ${UserContract.Columns.ID} = :userId limit 1")
    fun isDarkTheme(userId: Long): Flow<UserModel?>

    @Query("SELECT COUNT(${UserContract.Columns.ID}) FROM ${UserContract.TABLE_NAME}")
    fun getNumberOfUsers(): Long

    @Query("select * from ${UserContract.TABLE_NAME} where ${UserContract.Columns.ID} = :userId limit 1")
    fun getUser(userId: Long): UserModel

    @Query("select * from ${UserContract.TABLE_NAME} ORDER BY ${UserContract.Columns.TOKEN_DATE} DESC limit 1")
    fun getUserLastEntrance(): UserModel?

    @Query("select * from ${UserContract.TABLE_NAME} where ${UserContract.Columns.EMAIL} = :email limit 1")
    fun getUserByEmail(email: String): UserModel?

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserModel): Long

    @Update
    fun updateUser(user: UserModel)

    //--------------------------------------------------
    @Query(
        "UPDATE ${UserContract.TABLE_NAME} SET ${
            UserContract.Columns.DELETED_DATE
        } = :date WHERE ${UserContract.Columns.ID} = :userId"
    )
    suspend fun markDeletionUserById(userId: Long, date: Long = currentDateTimeInSecond())

    //--------------------------------------------------
    // тут удаление физически, т.е. то, что было удалено через сервер
    @Query("delete from ${UserContract.TABLE_NAME} where ${UserContract.Columns.ID} = :userId")
    fun deleteUserById(userId: Long)

}