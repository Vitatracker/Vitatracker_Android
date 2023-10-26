package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class SetNewPasswordNetworkModel(
    @SerializedName("message") val message: String? = null,
    @SerializedName("violations") val violations: ViolationsNetworkModel? = null,
)
