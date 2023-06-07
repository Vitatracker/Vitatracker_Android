package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class NotificationSetting(
    @SerializedName("id") val id: List<Long>,
    @SerializedName("userId") val userId: Long,
    @SerializedName("isEnabled") val isEnabled: Boolean,
    @SerializedName("isFloat") val isFloat: Boolean,
    @SerializedName("medicalControl") val medicalControl: Boolean,
    @SerializedName("nextCourseStart") val nextCourseStart: Boolean
)
