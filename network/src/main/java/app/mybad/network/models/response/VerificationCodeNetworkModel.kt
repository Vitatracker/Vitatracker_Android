package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class VerificationCodeNetworkModel(
    @SerializedName("token") val token: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("violations") val violations: ViolationsNetworkModel? = null,
)
