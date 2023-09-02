package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class PasswordNetworkModel(
    @SerializedName("message") val message: String? = null,
    @SerializedName("violations") val violations: ViolationsNetworkModel? = null,
)
data class ViolationsNetworkModel(
    @SerializedName("message") val message: String? = "",
    @SerializedName("fieldName") val fieldName: String? = "",
)
