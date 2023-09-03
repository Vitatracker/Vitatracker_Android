package app.mybad.network.models.request

import com.google.gson.annotations.SerializedName

data class UserChangePasswordRequestModel(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String,
)
