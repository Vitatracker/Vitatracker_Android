package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = FactUsageContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsageModel::class,
            parentColumns = [UsageContract.Columns.ID],
            childColumns = [FactUsageContract.Columns.USAGE_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(FactUsageContract.Columns.USAGE_ID)],
)
data class FactUsageModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FactUsageContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = FactUsageContract.Columns.IDN)
    val idn: Long = 0,

    @ColumnInfo(name = FactUsageContract.Columns.USAGE_ID)
    val usageId: Long,
    @ColumnInfo(name = FactUsageContract.Columns.USAGE_IDN)
    val usageIdn: Long,

    @ColumnInfo(name = FactUsageContract.Columns.FACT_USE_TIME)
    val factUseTime: Long = 0,

    @ColumnInfo(name = FactUsageContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = FactUsageContract.Columns.UPDATED_LOCAL_DATE)
    val updateLocalDate: Long = 0,
    @ColumnInfo(name = FactUsageContract.Columns.DELETED_DATE)
    val deleteLocalDate: Long = 0,
)
