package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class NotificationSettingNetworkModel(
    @SerializedName("user_id") val userId: String,
    @SerializedName("is_enabled") val isEnabled: Boolean,
    @SerializedName("is_float") val isFloat: Boolean,
    @SerializedName("medical_control") val medicationControl: Boolean,
    @SerializedName("next_course_start") val nextCourseStart: Boolean
)
