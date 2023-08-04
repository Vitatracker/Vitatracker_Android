package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = RemedyContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = [UserContract.Columns.ID],
            childColumns = [RemedyContract.Columns.USER_ID],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [Index(RemedyContract.Columns.USER_ID)],
)
data class RemedyModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RemedyContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = RemedyContract.Columns.IDN)
    val idn: Long = 0,

    @ColumnInfo(name = RemedyContract.Columns.CREATION_DATE)
    val creationDate: Long = 0,
    @ColumnInfo(name = RemedyContract.Columns.UPDATE_DATE)
    val updateDate: Long = 0,

    @ColumnInfo(name = RemedyContract.Columns.USER_ID)
    val userId: Long,
    @ColumnInfo(name = RemedyContract.Columns.USER_IDN)
    val userIdn: String,

    @ColumnInfo(name = RemedyContract.Columns.NAME)
    val name: String? = null,
    @ColumnInfo(name = RemedyContract.Columns.DESCRIPTION)
    val description: String? = null,
    @ColumnInfo(name = RemedyContract.Columns.COMMENT)
    val comment: String? = null,

    @ColumnInfo(name = RemedyContract.Columns.TYPE)
    val type: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.ICON)
    val icon: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.COLOR)
    val color: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.DOSE)
    val dose: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.MEASURE_UNIT)
    val measureUnit: Int = 0,
    @ColumnInfo(name = RemedyContract.Columns.PHOTO)
    val photo: String? = null,
    @ColumnInfo(name = RemedyContract.Columns.BEFORE_FOOD)
    val beforeFood: Int = 5,
    @ColumnInfo(name = RemedyContract.Columns.NOT_USED)
    val notUsed: Boolean = false,

    @ColumnInfo(name = RemedyContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = RemedyContract.Columns.UPDATED_LOCAL_DATE)
    val updateLocalDate: Long = 0,
)
