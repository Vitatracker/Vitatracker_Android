package app.mybad.network.models.response

import com.google.gson.annotations.SerializedName

data class AuthorizationToken(
    @SerializedName("token") private val token: String
)