package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class UsageNetworkModel(
    @SerializedName("id") val id: Long,
    @SerializedName("created") val createdDate: Long? = null,
    @SerializedName("fact_use_time") val factUseTime: Long,
    @SerializedName("not_used") val notUsed: Boolean,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("updated") val updatedDate: Long? = null,
    @SerializedName("use_time") val useTime: Long,
    @SerializedName("course_id") val courseId: Long? = null,
    @SerializedName("user_id") val userId: String? = null,
)
