package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class UsageWithNameAndDateModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsageContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = UsageContract.Columns.COURSE_ID)
    val courseId: Long,
    @ColumnInfo(name = UsageContract.Columns.USER_ID)
    val userId: Long,

    @ColumnInfo(name = UsageContract.Columns.USE_TIME)
    val useTime: Long, // тут полная дата со временем
    @ColumnInfo(name = UsageContract.Columns.FACT_USE_TIME)
    val factUseTime: Long = -1, // дата фактического приема

    @ColumnInfo(name = UsageContract.Columns.QUANTITY)
    val quantity: Float = 1f, // количество вещества за раз

    //  из курса
    @ColumnInfo(name = CourseContract.Columns.REMEDY_ID)
    val remedyId: Long,

    @ColumnInfo(name = CourseContract.Columns.START_DATE)
    val startDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.END_DATE)
    val endDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.IS_INFINITE)
    val isInfinite: Boolean = false,

    @ColumnInfo(name = CourseContract.Columns.REGIME)
    val regime: Int = 0,

    @ColumnInfo(name = CourseContract.Columns.SHOW_USAGE_TIME)
    val showUsageTime: Boolean = true,

    @ColumnInfo(name = CourseContract.Columns.IS_FINISHED)
    val isFinished: Boolean = false,
    @ColumnInfo(name = CourseContract.Columns.NOT_USED)
    val notUsed: Boolean = false,

    // из ремеди
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
