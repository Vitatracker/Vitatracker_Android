package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class Courses(
    @SerializedName("id") val id: Long,
    @SerializedName("remedyId") val remedyId: Long,
    @SerializedName("comment") val comment: String,
    @SerializedName("regime") val regime: Long,
    @SerializedName("startDate") val startDate: Long,
    @SerializedName("endDate") val endDate: Long,
    @SerializedName("remindDate") val remindDate: Long,
    @SerializedName("interval") val interval: Long,
    @SerializedName("isFinished") val isFinished: Boolean,
    @SerializedName("isInfinite") val isInfinite: Boolean,
    @SerializedName("notUsed") val notUsed: Boolean,
    @SerializedName("usages") val usages: List<Usages>?
)
