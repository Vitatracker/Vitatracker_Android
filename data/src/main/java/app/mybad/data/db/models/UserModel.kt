package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = UserContract.TABLE_NAME,
    indices = [Index(value = [UserContract.Columns.EMAIL], unique = true)],
)
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UserContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = UserContract.Columns.IDN)
    val idn: String = "",
    @ColumnInfo(name = UserContract.Columns.AVATAR)
    val avatar: String = "",
    @ColumnInfo(name = UserContract.Columns.CREATION_DATE)
    val createdDate: Long = 0,
    @ColumnInfo(name = UserContract.Columns.UPDATED_DATE)
    val updatedDate: Long = 0,
    @ColumnInfo(name = UserContract.Columns.NAME)
    val name: String = "",
    @ColumnInfo(name = UserContract.Columns.EMAIL)
    val email: String,
    @ColumnInfo(name = UserContract.Columns.PASSWORD)
    val password: String = "",
    @ColumnInfo(name = UserContract.Columns.NOT_USED)
    val notUsed: Boolean = false,

    @ColumnInfo(name = UserContract.Columns.TOKEN)
    val token: String = "",
    @ColumnInfo(name = UserContract.Columns.TOKEN_DATE)
    val tokenDate: Long = 0,

    @ColumnInfo(name = UserContract.Columns.TOKEN_REFRESH)
    val tokenRefresh: String = "",
    @ColumnInfo(name = UserContract.Columns.TOKEN_REFRESH_DATE)
    val tokenRefreshDate: Long = 0,

    @ColumnInfo(name = UserContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = UserContract.Columns.UPDATED_LOCAL_DATE)
    val updateLocalDate: Long = 0,
    @ColumnInfo(name = UserContract.Columns.DELETED_DATE)
    val deleteLocalDate: Long = 0,
    @ColumnInfo(name = UserContract.Columns.IS_DARK_THEME)
    val isDarkTheme: Long = 0,
)
