package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

// сверено с беком 10.07.23
data class UserNetworkModel(
    @SerializedName("id") val id: String,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("created") val createdDate: String = "",
    @SerializedName("email") val email: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("not_used") val notUsed: Boolean,
    @SerializedName("password") val password: String? = null,
    @SerializedName("updated") val updatedDate: String = "",
)
