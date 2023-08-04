package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class CourseNetworkModel(
    @SerializedName("id") val id: Long,// -> idn
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("created") val createdDate: String = "",
    @SerializedName("end_date") val endDate: Long,
    @SerializedName("interval") val interval: Long,
    @SerializedName("is_finished") val isFinished: Boolean,
    @SerializedName("is_infinite") val isInfinite: Boolean,
    @SerializedName("not_used") val notUsed: Boolean,
    @SerializedName("regime") val regime: Int,
    @SerializedName("remind_date") val remindDate: Long,
    @SerializedName("start_date") val startDate: Long,
    @SerializedName("updated") val updatedDate: String = "",
    @SerializedName("remedy_id") val remedyId: Long,
    @SerializedName("user_id") val userId: String? = null,
)
