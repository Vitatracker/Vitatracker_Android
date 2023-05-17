package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class Authorization(
    @SerializedName("token") val token: String,
    @SerializedName("id") val userId: String
)