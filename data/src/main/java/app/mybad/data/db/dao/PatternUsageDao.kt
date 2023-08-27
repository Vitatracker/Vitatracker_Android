package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.PatternUsageContract
import app.mybad.data.db.models.PatternUsageModel
import app.mybad.utils.currentDateTimeInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface PatternUsageDao {

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.USER_ID
        } = :userId and ${PatternUsageContract.Columns.DELETED_DATE} = 0"
    )
    fun getPatternUsages(userId: Long): Flow<List<PatternUsageModel>>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.USER_ID
        } = :userId and ${PatternUsageContract.Columns.DELETED_DATE} = 0"
    )
    suspend fun getPatternUsagesByUserId(userId: Long): List<PatternUsageModel>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun getPatternUsagesByCourseId(courseId: Long): List<PatternUsageModel>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId  and ${PatternUsageContract.Columns.USE_TIME} between :startTime and :endTime"
    )
    suspend fun getPatternUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): List<PatternUsageModel>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and  ${
            PatternUsageContract.Columns.USER_ID
        } = :userId and ${PatternUsageContract.Columns.USE_TIME} between :startTime and :endTime"
    )
    fun getPatternUsagesBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<PatternUsageModel>>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId and ${PatternUsageContract.Columns.USE_TIME} >= :time"
    )
    suspend fun getPatternUsagesAfterByCourseId(courseId: Long, time: Long): List<PatternUsageModel>

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatternUsage(pattern: PatternUsageModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatternUsages(patterns: List<PatternUsageModel>)

    //--------------------------------------------------
    @Query(
        "UPDATE ${PatternUsageContract.TABLE_NAME} SET ${
            PatternUsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            PatternUsageContract.Columns.ID
        } = :id"
    )
    suspend fun markDeletionPatternUsage(id: Long, date: Long = currentDateTimeInSecond())

    @Query(
        "UPDATE ${PatternUsageContract.TABLE_NAME} SET ${
            PatternUsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun markDeletionPatternUsagesByCourseId(
        courseId: Long,
        date: Long = currentDateTimeInSecond()
    )

    //--------------------------------------------------
    // тут удаление физически, т.е. то, что было удалено через сервер
    @Query(
        "delete from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.ID
        } = :id"
    )
    suspend fun deletePatternUsage(id: Long)

    @Query(
        "delete from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun deletePatternUsagesByUserId(userId: Long)

    @Delete
    suspend fun deletePatternUsages(patterns: List<PatternUsageModel>)

    //--------------------------------------------------
    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
            PatternUsageContract.Columns.USER_ID
        } = :userId and ${PatternUsageContract.Columns.UPDATED_NETWORK_DATE} = 0"
    )
    suspend fun getPatternUsagesNotUpdateByUserId(userId: Long): List<PatternUsageModel>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} > 0 and ${
            PatternUsageContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun getPatternUsagesDeletedByUserId(userId: Long): List<PatternUsageModel>
}
