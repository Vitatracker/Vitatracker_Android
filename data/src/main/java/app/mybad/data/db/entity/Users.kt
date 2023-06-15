package app.mybad.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_common")
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val mail: String,
)
