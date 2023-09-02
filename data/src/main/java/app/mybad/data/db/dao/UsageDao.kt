package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.UsageContract
import app.mybad.data.db.models.UsageModel
import app.mybad.utils.currentDateTimeInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.USER_ID
        } = :userId and ${UsageContract.Columns.DELETED_DATE} = 0"
    )
    fun getUsages(userId: Long): Flow<List<UsageModel>>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.USER_ID
        } = :userId and ${UsageContract.Columns.DELETED_DATE} = 0"
    )
    suspend fun getUsagesByUserId(userId: Long): List<UsageModel>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun getUsagesByCourseId(courseId: Long): List<UsageModel>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.ID
        } = :usageId  limit 1"
    )
    suspend fun getUsageById(usageId: Long): UsageModel?

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.COURSE_ID
        } = :courseId  and ${
            UsageContract.Columns.USE_TIME
        } between :startTime and :endTime"
    )
    suspend fun getUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): List<UsageModel>

    @Query(
        "SELECT * FROM ${UsageContract.TABLE_NAME} WHERE ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.USER_ID
        } = :userId and ${UsageContract.Columns.USE_TIME} BETWEEN :startTime AND :endTime"
    )
    fun getUsagesBetween(userId: Long, startTime: Long, endTime: Long): Flow<List<UsageModel>>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} >= :time"
    )
    suspend fun getUsagesAfterByCourseId(courseId: Long, time: Long): List<UsageModel>

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: UsageModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsages(usages: List<UsageModel>)
    //--------------------------------------------------

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.FACT_USE_TIME} > 0 limit 1"
    )
    suspend fun checkUseUsagesByCourseId(courseId: Long): UsageModel?

    //--------------------------------------------------
    @Query(
        "UPDATE ${UsageContract.TABLE_NAME} SET ${
            UsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            UsageContract.Columns.ID
        } = :usageId"
    )
    suspend fun markDeletionUsagesById(usageId: Long, date: Long = currentDateTimeInSecond())

    @Query(
        "UPDATE ${UsageContract.TABLE_NAME} SET ${
            UsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            UsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun markDeletionUsagesByCourseId(courseId: Long, date: Long = currentDateTimeInSecond())

    @Query(
        "UPDATE ${UsageContract.TABLE_NAME} SET ${
            UsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} between :startTime and :endTime"
    )
    suspend fun markDeletionUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long,
        date: Long = currentDateTimeInSecond()
    )

    @Query(
        "UPDATE ${UsageContract.TABLE_NAME} SET ${
            UsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${
            UsageContract.Columns.FACT_USE_TIME
        } <= 0 and ${
            UsageContract.Columns.USE_TIME
        } >= :dateTime"
    )
    suspend fun markDeletionUsagesAfterByCourseId(
        courseId: Long,
        dateTime: Long,
        date: Long = currentDateTimeInSecond()
    )

    //--------------------------------------------------
    // тут удаление физически, т.е. то, что было удалено через сервер
    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.ID
        } = :usageId"
    )
    suspend fun deleteUsagesById(usageId: Long)

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun deleteUsagesByUserId(userId: Long)

    @Delete
    suspend fun deleteUsages(usages: List<UsageModel>)

    //--------------------------------------------------
    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.USER_ID
        } = :userId and ${UsageContract.Columns.UPDATED_NETWORK_DATE} = 0"
    )
    suspend fun getUsagesNotUpdateByUserId(userId: Long): List<UsageModel>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} > 0 and ${
            UsageContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getUsagesDeletedByUserId(userId: Long): List<UsageModel>
}
