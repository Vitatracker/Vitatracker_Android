package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class UsageNetworkModel(
    @SerializedName("id") val id: Long,

    @SerializedName("useTime") val useTime: Long,
    @SerializedName("factUseTime") val factUseTime: Long,
    @SerializedName("quantity") val quantity: Float,

    @SerializedName("notUsed") val notUsed: Boolean,

    @SerializedName("courseId") val courseId: Long? = null,
    @SerializedName("userId") val userId: String? = null,

    @SerializedName("created") val createdDate: String = "",
    @SerializedName("updated") val updatedDate: String = "",
)
