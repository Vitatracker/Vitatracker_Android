package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.CourseContract
import app.mybad.data.db.models.RemedyContract
import app.mybad.data.db.models.UsageContract
import app.mybad.data.db.models.UsageModel
import app.mybad.data.db.models.UsageWithNameAndDateModel
import app.mybad.utils.currentDateTimeUTCInSecond
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
            UsageContract.Columns.USER_ID
        } = :userId  and ${
            UsageContract.Columns.COURSE_ID
        } = :courseId  and ${
            UsageContract.Columns.USE_TIME
        } = :useTime  limit 1"
    )
    suspend fun getUsageByParams(
        userId: Long,
        courseId: Long,
        useTime: Long,
    ): UsageModel?

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
        } = :userId and ${UsageContract.Columns.USE_TIME} between :startTime and :endTime order by ${UsageContract.Columns.USE_TIME}"
    )
    fun getUsagesBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): List<UsageModel>

    @Query(
        "select A.${UsageContract.Columns.ID}, A.${
            UsageContract.Columns.COURSE_ID
        }, A.${UsageContract.Columns.USER_ID}, A.${
            UsageContract.Columns.USE_TIME
        }, A.${UsageContract.Columns.FACT_USE_TIME}, A.${
            UsageContract.Columns.QUANTITY
        }, B.${CourseContract.Columns.IDN}, B.${
            CourseContract.Columns.REMEDY_ID
        }, B.${CourseContract.Columns.START_DATE}, B.${
            CourseContract.Columns.END_DATE
        }, B.${CourseContract.Columns.IS_INFINITE}, B.${
            CourseContract.Columns.REGIME
        }, B.${
            CourseContract.Columns.SHOW_USAGE_TIME
        }, B.${CourseContract.Columns.IS_FINISHED}, B.${
            CourseContract.Columns.NOT_USED
        }, C.${RemedyContract.Columns.NAME}, C.${
            RemedyContract.Columns.DESCRIPTION
        }, C.${
            RemedyContract.Columns.TYPE
        }, C.${
            RemedyContract.Columns.ICON
        }, C.${
            RemedyContract.Columns.COLOR
        }, C.${
            RemedyContract.Columns.DOSE
        }, C.${
            RemedyContract.Columns.BEFORE_FOOD
        }, C.${
            RemedyContract.Columns.MEASURE_UNIT
        }, C.${
            RemedyContract.Columns.PHOTO
        } from ${
            UsageContract.TABLE_NAME
        } A LEFT JOIN ${
            CourseContract.TABLE_NAME
        } B ON A.${UsageContract.Columns.COURSE_ID} = B.${CourseContract.Columns.ID} LEFT JOIN ${
            RemedyContract.TABLE_NAME
        } C ON B.${CourseContract.Columns.REMEDY_ID} = C.${
            RemedyContract.Columns.ID
        } where A.${UsageContract.Columns.USER_ID} = :userId and B.${
            CourseContract.Columns.DELETED_DATE
        } = 0 and B.${CourseContract.Columns.IS_FINISHED} = 0 and B.${
            CourseContract.Columns.NOT_USED
        } = 0 and A.${UsageContract.Columns.USE_TIME} between :startTime and :endTime order by A.${
            UsageContract.Columns.USE_TIME
        }, C.${RemedyContract.Columns.NAME}"
    )
    fun getUsagesWithNameAndDateBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageWithNameAndDateModel>>

    @Query(
        "select A.${UsageContract.Columns.ID}, A.${
            UsageContract.Columns.COURSE_ID
        }, A.${UsageContract.Columns.USER_ID}, A.${
            UsageContract.Columns.USE_TIME
        }, A.${UsageContract.Columns.FACT_USE_TIME}, A.${
            UsageContract.Columns.QUANTITY
        }, B.${CourseContract.Columns.IDN}, B.${
            CourseContract.Columns.REMEDY_ID
        }, B.${CourseContract.Columns.START_DATE}, B.${
            CourseContract.Columns.END_DATE
        }, B.${CourseContract.Columns.IS_INFINITE}, B.${
            CourseContract.Columns.REGIME
        }, B.${
            CourseContract.Columns.SHOW_USAGE_TIME
        }, B.${CourseContract.Columns.IS_FINISHED}, B.${
            CourseContract.Columns.NOT_USED
        }, C.${RemedyContract.Columns.NAME}, C.${
            RemedyContract.Columns.DESCRIPTION
        }, C.${
            RemedyContract.Columns.TYPE
        }, C.${
            RemedyContract.Columns.ICON
        }, C.${
            RemedyContract.Columns.COLOR
        }, C.${
            RemedyContract.Columns.DOSE
        }, C.${
            RemedyContract.Columns.BEFORE_FOOD
        }, C.${
            RemedyContract.Columns.MEASURE_UNIT
        }, C.${
            RemedyContract.Columns.PHOTO
        } from ${
            UsageContract.TABLE_NAME
        } A LEFT JOIN ${
            CourseContract.TABLE_NAME
        } B ON A.${UsageContract.Columns.COURSE_ID} = B.${CourseContract.Columns.ID} LEFT JOIN ${
            RemedyContract.TABLE_NAME
        } C ON B.${CourseContract.Columns.REMEDY_ID} = C.${
            RemedyContract.Columns.ID
        } where A.${UsageContract.Columns.USER_ID} = :userId and B.${
            CourseContract.Columns.DELETED_DATE
        } = 0 and A.${UsageContract.Columns.USE_TIME} between :startTime and :endTime order by A.${
            UsageContract.Columns.USE_TIME
        }, C.${RemedyContract.Columns.NAME}"
    )
    fun getUsagesWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageWithNameAndDateModel>>

    @Query(
        "select * from ${UsageContract.TABLE_NAME} where ${UsageContract.Columns.DELETED_DATE} = 0 and ${
            UsageContract.Columns.COURSE_ID
        } = :courseId and ${UsageContract.Columns.USE_TIME} >= :time"
    )
    suspend fun getUsagesAfterByCourseId(courseId: Long, time: Long): List<UsageModel>

    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: UsageModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsages(usages: List<UsageModel>)


    @Query(
        "UPDATE ${UsageContract.TABLE_NAME} SET ${
            UsageContract.Columns.FACT_USE_TIME
        } = :factUseTime, ${
            UsageContract.Columns.UPDATED_DATE
        } = :date, ${
            UsageContract.Columns.UPDATED_NETWORK_DATE
        } = 0 WHERE ${
            UsageContract.Columns.ID
        } = :usageId"
    )
    suspend fun setFactUseTimeUsage(
        usageId: Long,
        factUseTime: Long?,
        date: Long = currentDateTimeUTCInSecond()
    )
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
    suspend fun markDeletionUsagesById(usageId: Long, date: Long = currentDateTimeUTCInSecond())

    @Query(
        "UPDATE ${UsageContract.TABLE_NAME} SET ${
            UsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            UsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun markDeletionUsagesByCourseId(courseId: Long, date: Long = currentDateTimeUTCInSecond())

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
        date: Long = currentDateTimeUTCInSecond()
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
        } >= :date"
    )
    suspend fun markDeletionUsagesAfterByCourseId(
        courseId: Long,
        date: Long = currentDateTimeUTCInSecond()
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

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun deleteUsagesByCourseId(courseId: Long)

    @Query(
        "delete from ${UsageContract.TABLE_NAME} where ${
            UsageContract.Columns.COURSE_IDN
        } = :courseIdn"
    )
    suspend fun deleteUsagesByCourseIdn(courseIdn: Long)

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
