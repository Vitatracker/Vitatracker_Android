package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class AuthorizationNetworkModel(
    @SerializedName("accessToken") val token: String,
    @SerializedName("refreshToken") val refreshToken: String,
)
