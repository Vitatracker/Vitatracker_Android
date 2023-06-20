package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.mybad.data.db.entity.UserLocalDataModel

@Dao
interface UsersDao {

    @Query("select id from users_common where email=:email limit 1")
    fun getUserId(email: String): Long?

    @Query("select * from users_common where id=:userId limit 1")
    fun getUser(userId: Long): UserLocalDataModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserLocalDataModel): Long?

    @Update
    fun updateUser(user: UserLocalDataModel)

    @Query("delete from users_common where id=:userId")
    fun deleteFromUserId(userId: Long)

}
