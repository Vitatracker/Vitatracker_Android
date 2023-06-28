package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class Authorization(
    @SerializedName("accessToken") val token: String,
    @SerializedName("refreshToken") val refreshToken: String,
)
