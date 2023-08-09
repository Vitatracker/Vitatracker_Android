package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class RemedyNetworkModel(
    @SerializedName("id") val id: Long,// -> idn
    @SerializedName("beforeFood") val beforeFood: Int,
    @SerializedName("color") val color: Int,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("created") val createdDate: String = "",
    @SerializedName("description") val description: String? = null,
    @SerializedName("dose") val dose: Int,
    @SerializedName("icon") val icon: Int,
    @SerializedName("measureUnit") val measureUnit: Int,
    @SerializedName("name") val name: String? = null,
    @SerializedName("notUsed") val notUsed: Boolean,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("type") val type: Int,
    @SerializedName("updated") val updatedDate: String = "",
    @SerializedName("userId") val userId: String?,
)
