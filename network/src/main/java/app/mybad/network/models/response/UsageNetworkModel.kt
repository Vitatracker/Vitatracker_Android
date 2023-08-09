package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class UsageNetworkModel(
    @SerializedName("id") val id: Long,
    @SerializedName("created") val createdDate: String = "",
    @SerializedName("factUseTime") val factUseTime: Long,
    @SerializedName("notUsed") val notUsed: Boolean,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("updated") val updatedDate: String = "",
    @SerializedName("useTime") val useTime: Long,
    @SerializedName("courseId") val courseId: Long? = null,
    @SerializedName("remedyId") val remedyId: Long? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
)
