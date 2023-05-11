package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("notUsed") val notUsed: Boolean?,
    @SerializedName("notificationSettings") val notificationSettings: NotificationSetting?,
    @SerializedName("remedies") val remedies: List<Remedies>?,
)