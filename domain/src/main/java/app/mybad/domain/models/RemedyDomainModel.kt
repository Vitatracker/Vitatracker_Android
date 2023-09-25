package app.mybad.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemedyDomainModel(
    val id: Long = 0,
    var idn: Long = 0,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,

    val type: Int = 0,
    val icon: Int = 0,
    val color: Int = 0,
    val dose: Int = 0,
    val measureUnit: Int = 0,
    val photo: String? = null,
    val beforeFood: Int = 1,
    val notUsed: Boolean = false,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemedyDomainModel

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (comment != other.comment) return false
        if (type != other.type) return false
        if (icon != other.icon) return false
        if (color != other.color) return false
        if (dose != other.dose) return false
        if (measureUnit != other.measureUnit) return false
        if (photo != other.photo) return false
        return beforeFood == other.beforeFood
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + type
        result = 31 * result + icon
        result = 31 * result + color
        result = 31 * result + dose
        result = 31 * result + measureUnit
        result = 31 * result + (photo?.hashCode() ?: 0)
        result = 31 * result + beforeFood
        return result
    }
}

