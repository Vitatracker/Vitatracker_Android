package app.mybad.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users_common",
    indices = [Index(value = ["email"], unique = true)],
)
data class UserLocalDataModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val email: String,
)
