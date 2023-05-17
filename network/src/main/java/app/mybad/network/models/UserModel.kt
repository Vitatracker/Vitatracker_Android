package app.mybad.network.models

import app.mybad.network.models.response.NotificationSetting
import app.mybad.network.models.response.Remedies
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String = "123456",
    @SerializedName("avatar") val avatar: String,
    @SerializedName("notUsed") val notUsed: Boolean?,
    @SerializedName("notificationSettings") val notificationSettings: NotificationSetting?,
    @SerializedName("remedies") val remedies: List<Remedies>?,
)
