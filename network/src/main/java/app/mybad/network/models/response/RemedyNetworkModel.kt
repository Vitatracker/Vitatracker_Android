package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class RemedyNetworkModel(
    @SerializedName("id") val id: Long,// -> idn
    @SerializedName("before_food") val beforeFood: Int,
    @SerializedName("color") val color: Int,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("created") val createdDate: String = "",
    @SerializedName("description") val description: String? = null,
    @SerializedName("dose") val dose: Int,
    @SerializedName("icon") val icon: Int,
    @SerializedName("measure_unit") val measureUnit: Int,
    @SerializedName("name") val name: String? = null,
    @SerializedName("not_used") val notUsed: Boolean,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("type") val type: Int,
    @SerializedName("updated") val updatedDate: String = "",
    @SerializedName("user_id") val userId: String?,
)
