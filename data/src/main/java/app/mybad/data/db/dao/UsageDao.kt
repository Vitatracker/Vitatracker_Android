package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.UsageContract
import app.mybad.data.db.models.UsageModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.USER_ID
        } = :userId"
    )
    fun getUsages(userId: Long): Flow<List<UsageModel>>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getUsagesByUserId(userId: Long): List<UsageModel>

    @Query("select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.COURSE_ID} = :courseId")
    suspend fun getUsagesByCourseId(courseId: Long): List<UsageModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: UsageModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsages(usages: List<UsageModel>)

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} between :startTime and :endTime"
    )
    suspend fun getUsagesBetweenById(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): List<UsageModel>

    @Query(
        "SELECT * FROM ${UsageContract.TABLE_NAME} WHERE ${
            UsageContract.Columns.USE_TIME
        } BETWEEN :startTime AND :endTime"
    )
    suspend fun getUsagesBetween(startTime: Long, endTime: Long): List<UsageModel>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} >= :time"
    )
    suspend fun getUsagesAfter(courseId: Long, time: Long): List<UsageModel>

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.ID
        } = :usageId"
    )
    suspend fun deleteUsagesById(usageId: Long)

    @Delete
    suspend fun deleteUsages(usages: List<UsageModel>)

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun deleteUsagesByCourseId(courseId: Long)

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} between :startTime and :endTime"
    )
    suspend fun deleteUsagesBetweenById(courseId: Long, startTime: Long, endTime: Long)

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} >= :time"
    )
    suspend fun deleteUsagesAfter(courseId: Long, time: Long)

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.USER_ID
        } = :userId and ${UsageContract.Columns.UPDATED_NETWORK_DATE} = 0"
    )
    suspend fun getUsagesNotUpdateByUserId(userId: Long): List<UsageModel>
}
