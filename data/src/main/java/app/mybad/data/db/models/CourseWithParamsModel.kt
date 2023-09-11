package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CourseWithParamsModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CourseContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.IDN)
    val idn: Long = 0,

    @ColumnInfo(name = CourseContract.Columns.USER_ID)
    val userId: Long,
    @ColumnInfo(name = CourseContract.Columns.USER_IDN)
    val userIdn: String = "",

    @ColumnInfo(name = CourseContract.Columns.REMEDY_ID)
    val remedyId: Long,
    @ColumnInfo(name = CourseContract.Columns.REMEDY_IDN)
    val remedyIdn: Long = 0,

    @ColumnInfo(name = CourseContract.Columns.START_DATE)
    val startDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.END_DATE)
    val endDate: Long = 0,

    @ColumnInfo(name = CourseContract.Columns.REGIME)
    val regime: Int = 0,
    @ColumnInfo(name = CourseContract.Columns.SHOW_USAGE_TIME)
    val showUsageTime: Boolean = true,
    @ColumnInfo(name = CourseContract.Columns.IS_FINISHED)
    val isFinished: Boolean = false,
    @ColumnInfo(name = CourseContract.Columns.IS_INFINITE)
    val isInfinite: Boolean = false,
    @ColumnInfo(name = CourseContract.Columns.NOT_USED)
    val notUsed: Boolean = false,

    @ColumnInfo(name = CourseContract.Columns.PATTERN_USAGES)
    val patternUsages: String = "",

    @ColumnInfo(name = CourseContract.Columns.COMMENT)
    val comment: String = "",

    @ColumnInfo(name = CourseContract.Columns.REMIND_DATE)
    val remindDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.INTERVAL)
    val interval: Long = 0,

    @ColumnInfo(name = CourseContract.Columns.CREATION_DATE)
    val createdDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.UPDATE_DATE)
    val updateDate: Long = 0,

    @ColumnInfo(name = CourseContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.DELETED_DATE)
    val deleteLocalDate: Long = 0,

    // из remedy
    @ColumnInfo(name = RemedyContract.Columns.NAME)
    val name: String? = null,
    @ColumnInfo(name = RemedyContract.Columns.DESCRIPTION)
    val description: String? = null,

    @ColumnInfo(name = RemedyContract.Columns.TYPE)
    val type: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.ICON)
    val icon: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.COLOR)
    val color: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.DOSE)
    val dose: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.BEFORE_FOOD)
    val beforeFood: Int = 5,

    @ColumnInfo(name = RemedyContract.Columns.MEASURE_UNIT)
    val measureUnit: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.PHOTO)
    val photo: String? = null,
)
