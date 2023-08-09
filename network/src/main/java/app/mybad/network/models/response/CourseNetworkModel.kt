package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class CourseNetworkModel(
    @SerializedName("id") val id: Long,// -> idn
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("created") val createdDate: String = "",
    @SerializedName("endDate") val endDate: Long,
    @SerializedName("interval") val interval: Long,
    @SerializedName("isFinished") val isFinished: Boolean,
    @SerializedName("isInfinite") val isInfinite: Boolean,
    @SerializedName("notUsed") val notUsed: Boolean,
    @SerializedName("regime") val regime: Int,
    @SerializedName("remindDate") val remindDate: Long,
    @SerializedName("startDate") val startDate: Long,
    @SerializedName("updated") val updatedDate: String = "",
    @SerializedName("remedyId") val remedyId: Long,
    @SerializedName("userId") val userId: String? = null,
)
