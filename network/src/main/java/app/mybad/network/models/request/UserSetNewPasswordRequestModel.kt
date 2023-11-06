package app.mybad.network.models.request

import com.google.gson.annotations.SerializedName

data class UserSetNewPasswordRequestModel(
    @SerializedName("token") val token: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)
