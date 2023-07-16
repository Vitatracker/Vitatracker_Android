package app.mybad.network.models.request

import com.google.gson.annotations.SerializedName

data class UserRegistrationRequestModel(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)
