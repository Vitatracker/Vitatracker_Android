package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.CourseContract
import app.mybad.data.db.models.PatternUsageContract
import app.mybad.data.db.models.PatternUsageFutureWithNameAndDateModel
import app.mybad.data.db.models.PatternUsageModel
import app.mybad.data.db.models.PatternUsageWithNameAndDateModel
import app.mybad.data.db.models.RemedyContract
import app.mybad.utils.currentDateTimeUTCInSecond
import kotlinx.coroutines.flow.Flow

@Dao
interface PatternUsageDao {

    @Query(
        "select A.${
            PatternUsageContract.Columns.ID
        }, A.${PatternUsageContract.Columns.COURSE_ID}, A.${
            PatternUsageContract.Columns.USER_ID
        }, A.${
            PatternUsageContract.Columns.TIME_MINUTES
        }, A.${PatternUsageContract.Columns.QUANTITY}, B.${
            CourseContract.Columns.IDN
        }, B.${
            CourseContract.Columns.REMEDY_ID
        }, B.${
            CourseContract.Columns.START_DATE
        }, B.${
            CourseContract.Columns.END_DATE
        }, B.${
            CourseContract.Columns.IS_INFINITE
        }, B.${CourseContract.Columns.REGIME}, B.${
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
            PatternUsageContract.TABLE_NAME
        } A LEFT JOIN ${
            CourseContract.TABLE_NAME
        } B ON A.${PatternUsageContract.Columns.COURSE_ID} = B.${CourseContract.Columns.ID} LEFT JOIN ${
            RemedyContract.TABLE_NAME
        } C ON B.${CourseContract.Columns.REMEDY_ID} = C.${RemedyContract.Columns.ID} where A.${
            PatternUsageContract.Columns.USER_ID
        } = :userId and A.${PatternUsageContract.Columns.IS_FINISHED} = 0 and B.${
            CourseContract.Columns.DELETED_DATE
        } = 0 and B.${
            CourseContract.Columns.IS_FINISHED
        } = 0 and B.${CourseContract.Columns.NOT_USED} = 0 and ((B.${CourseContract.Columns.IS_INFINITE} and :endTime >= B.${
            CourseContract.Columns.START_DATE
        }) or (:endTime >= B.${CourseContract.Columns.START_DATE} and :startTime <= B.${
            CourseContract.Columns.END_DATE
        })) order by A.${PatternUsageContract.Columns.ID}, C.${RemedyContract.Columns.NAME}"
    )
    fun getPatternUsagesWithNameAndDateBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<PatternUsageWithNameAndDateModel>>

    @Query(
        "select A.${
            PatternUsageContract.Columns.ID
        }, A.${PatternUsageContract.Columns.COURSE_ID}, A.${
            PatternUsageContract.Columns.USER_ID
        }, A.${
            PatternUsageContract.Columns.TIME_MINUTES
        }, A.${PatternUsageContract.Columns.QUANTITY}, B.${
            CourseContract.Columns.IDN
        }, B.${
            CourseContract.Columns.REMEDY_ID
        }, B.${
            CourseContract.Columns.START_DATE
        }, B.${
            CourseContract.Columns.END_DATE
        }, B.${
            CourseContract.Columns.IS_INFINITE
        }, B.${CourseContract.Columns.REGIME}, B.${
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
            PatternUsageContract.TABLE_NAME
        } A LEFT JOIN ${
            CourseContract.TABLE_NAME
        } B ON A.${PatternUsageContract.Columns.COURSE_ID} = B.${CourseContract.Columns.ID} LEFT JOIN ${
            RemedyContract.TABLE_NAME
        } C ON B.${CourseContract.Columns.REMEDY_ID} = C.${RemedyContract.Columns.ID
        } where A.${PatternUsageContract.Columns.USER_ID} = :userId and B.${CourseContract.Columns.DELETED_DATE
        } = 0 and ((B.${CourseContract.Columns.IS_INFINITE} and :endTime >= B.${
            CourseContract.Columns.START_DATE
        }) or (:endTime >= B.${CourseContract.Columns.START_DATE} and :startTime <= B.${
            CourseContract.Columns.END_DATE
        })) order by A.${PatternUsageContract.Columns.ID}, C.${RemedyContract.Columns.NAME}"
    )
    fun getPatternUsagesWithParamsBetween( // тут должны попасть и закрытые и законченные курсы
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<PatternUsageWithNameAndDateModel>>

    @Query(
        "select A.${
            PatternUsageContract.Columns.ID
        }, A.${PatternUsageContract.Columns.COURSE_ID}, A.${
            PatternUsageContract.Columns.USER_ID
        }, A.${
            PatternUsageContract.Columns.TIME_MINUTES
        }, A.${PatternUsageContract.Columns.QUANTITY}, B.${
            CourseContract.Columns.IDN
        }, B.${
            CourseContract.Columns.REMEDY_ID
        }, (B.${CourseContract.Columns.END_DATE} + B.${CourseContract.Columns.INTERVAL} * 86400) as ${
            CourseContract.Columns.START_DATE_FUTURE
        }, (B.${CourseContract.Columns.END_DATE} + B.${CourseContract.Columns.INTERVAL} * 86400 + B.${CourseContract.Columns.END_DATE} - B.${CourseContract.Columns.START_DATE}) as ${
            CourseContract.Columns.END_DATE_FUTURE
        }, B.${
            CourseContract.Columns.END_DATE
        }, B.${
            CourseContract.Columns.IS_INFINITE
        }, B.${CourseContract.Columns.REGIME}, B.${
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
            PatternUsageContract.TABLE_NAME
        } A LEFT JOIN ${
            CourseContract.TABLE_NAME
        } B ON A.${PatternUsageContract.Columns.COURSE_ID} = B.${CourseContract.Columns.ID} LEFT JOIN ${
            RemedyContract.TABLE_NAME
        } C ON B.${CourseContract.Columns.REMEDY_ID} = C.${RemedyContract.Columns.ID
        } where A.${PatternUsageContract.Columns.USER_ID} = :userId and B.${CourseContract.Columns.DELETED_DATE
        } = 0 and B.${CourseContract.Columns.INTERVAL
        } > 0 and :endTime >= ${
            CourseContract.Columns.START_DATE_FUTURE
        } and (:startTime <= ${
            CourseContract.Columns.END_DATE_FUTURE
        } or B.${CourseContract.Columns.IS_INFINITE
        } > 0) order by A.${PatternUsageContract.Columns.ID}, C.${RemedyContract.Columns.NAME}"
    )
    fun getFutureWithParamsBetween( // тут только будущее, где INTERVAL > 0
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<PatternUsageFutureWithNameAndDateModel>>

//    @Query(
//        "select A.id, A.course_id, A.user_id, A.time_minutes, A.quantity, B.remedy_id, B.start_date, B.end_date, B.is_infinite, B.regime, B.show_usage_time, B.not_used , B.is_finished  from ${
//            PatternUsageContract.TABLE_NAME
//        } A LEFT JOIN ${CourseContract.TABLE_NAME} B ON A.course_id = B.id LEFT JOIN ${
//            RemedyContract.TABLE_NAME
//        } C ON B.remedy_id = C.id where A.user_id = :userId and B.deleted_local = 0"
//    )
//    fun getPatternUsagesWithNameAndDateByUserId(userId: Long): Flow<List<PatternUsageWithNameAndDateModel>>
//
//    @Query(
//        "select A.id, A.course_id, A.user_id, A.time_minutes, A.quantity, B.remedy_id, B.start_date, B.end_date, B.is_infinite, B.regime, B.show_usage_time, B.not_used , B.is_finished  from ${
//            PatternUsageContract.TABLE_NAME
//        } A LEFT JOIN ${CourseContract.TABLE_NAME} B ON A.course_id = B.id where ${
//            PatternUsageContract.Columns.USER_ID
//        } = :userId and ${PatternUsageContract.Columns.DELETED_DATE} = 0"
//    )
//    fun getPatternUsagesWithDate(userId: Long): Flow<List<PatternUsageWithDateModel>>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.USER_ID
        } = :userId and ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${PatternUsageContract.Columns.IS_FINISHED} = 0"
    )
    fun getPatternUsages(userId: Long): Flow<List<PatternUsageModel>>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.USER_ID
        } = :userId and ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${PatternUsageContract.Columns.IS_FINISHED} = 0"
    )
    suspend fun getPatternUsagesByUserId(userId: Long): List<PatternUsageModel>

    @Query(
        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun getPatternUsagesByCourseId(courseId: Long): List<PatternUsageModel>

    //    @Query(
//        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
//            PatternUsageContract.Columns.COURSE_ID
//        } = :courseId  and ${PatternUsageContract.Columns.TIME_MINUTES} between :startTime and :endTime"
//    )
//    suspend fun getPatternUsagesBetweenByCourseId(
//        courseId: Long,
//        startTime: Long,
//        endTime: Long
//    ): List<PatternUsageModel>
//
////    @Query(
////        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and  ${
////            PatternUsageContract.Columns.USER_ID
////        } = :userId and ${PatternUsageContract.Columns.USE_TIME} between :startTime and :endTime"
////    )
////    fun getPatternUsagesBetween(
////        userId: Long,
////        startTime: Long,
////        endTime: Long
////    ): Flow<List<PatternUsageModel>>
////
//    @Query(
//        "select * from ${PatternUsageContract.TABLE_NAME} where ${PatternUsageContract.Columns.DELETED_DATE} = 0 and ${
//            PatternUsageContract.Columns.COURSE_ID
//        } = :courseId and ${PatternUsageContract.Columns.USE_TIME} >= :time"
//    )
//    suspend fun getPatternUsagesAfterByCourseId(courseId: Long, time: Long): List<PatternUsageModel>
//
    //--------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatternUsage(pattern: PatternUsageModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatternUsages(patterns: List<PatternUsageModel>)

    //--------------------------------------------------
    @Query(
        "UPDATE ${PatternUsageContract.TABLE_NAME} SET ${
            PatternUsageContract.Columns.IS_FINISHED
        } = 1 WHERE ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun finishedPatternUsageByCourseId(courseId: Long)

    //--------------------------------------------------
    @Query(
        "UPDATE ${PatternUsageContract.TABLE_NAME} SET ${
            PatternUsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            PatternUsageContract.Columns.ID
        } = :id"
    )
    suspend fun markDeletionPatternUsage(id: Long, date: Long = currentDateTimeUTCInSecond())

    @Query(
        "UPDATE ${PatternUsageContract.TABLE_NAME} SET ${
            PatternUsageContract.Columns.DELETED_DATE
        } = :date WHERE ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun markDeletionPatternUsagesByCourseId(
        courseId: Long,
        date: Long = currentDateTimeUTCInSecond()
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

    @Query(
        "delete from ${PatternUsageContract.TABLE_NAME} where ${
            PatternUsageContract.Columns.COURSE_ID
        } = :courseId"
    )
    suspend fun deletePatternUsagesByCourseId(courseId: Long)

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
