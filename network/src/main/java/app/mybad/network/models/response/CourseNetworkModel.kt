package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class CourseNetworkModel(
    @SerializedName("id") val id: Long,// -> idn

    @SerializedName("startDate") val startDate: Long,
    @SerializedName("endDate") val endDate: Long,

    @SerializedName("comment") val comment: String? = null,

    @SerializedName("remedyId") val remedyId: Long,
    @SerializedName("regime") val regime: Int,
    @SerializedName("patternUsage") val patternUsages: String? = null,

    @SerializedName("notUsed") val notUsed: Boolean,
    @SerializedName("isInfinite") val isInfinite: Boolean,
    @SerializedName("isFinished") val isFinished: Boolean,

    @SerializedName("remindDate") val remindDate: Long,
    @SerializedName("interval") val interval: Long,

    @SerializedName("userId") val userId: String? = null,

    @SerializedName("created") val createdDate: String = "",
    @SerializedName("updated") val updatedDate: String = "",
)
