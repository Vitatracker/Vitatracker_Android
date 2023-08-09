package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class NotificationSettingNetworkModel(
    @SerializedName("userId") val userId: String,
    @SerializedName("isEnabled") val isEnabled: Boolean,
    @SerializedName("isFloat") val isFloat: Boolean,
    @SerializedName("medicalControl") val medicationControl: Boolean,
    @SerializedName("nextCourseStart") val nextCourseStart: Boolean
)
