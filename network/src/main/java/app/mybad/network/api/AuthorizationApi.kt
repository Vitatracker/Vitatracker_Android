package app.mybad.network.api

import app.mybad.network.models.request.UserLoginRequestModel
import app.mybad.network.models.request.UserRegistrationRequestModel
import app.mybad.network.models.response.AuthorizationNetworkModel
import retrofit2.http.Body
import retrofit2.http.POST
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface AuthorizationApi {

    @POST(value = "api/v1/auth/authenticate")
    suspend fun loginUser(@Body loginModel: UserLoginRequestModel): AuthorizationNetworkModel

    @POST(value = "api/v1/auth/register")
    suspend fun registrationUser(@Body registrationModel: UserRegistrationRequestModel): AuthorizationNetworkModel

    @POST(value = "api/v1/auth/refresh-token")
    suspend fun refreshToken(): AuthorizationNetworkModel

    companion object {
        // token date expires
        private val regexDateExp = """"exp":(\d+)""".toRegex()

        //"accessToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4OGU0ODVhOC1lMDY1LTRiZDAtODlhZi1hMjk3OTY0ZTQ0NDYiLCJpYXQiOjE2OTE2MTEzNjgsImV4cCI6MTY5MTY5Nzc2OH0.Eo9XYAgODi_nycMFl-wgtf7sDeAdwVlu9_FZKZiz3JE"
        @OptIn(ExperimentalEncodingApi::class)
        fun decodeTokenDateExp(token: String): Long = if (token.isBlank()) 0L
        else String(Base64.decode(token.split(".")[1])).let {
            regexDateExp.find(it)?.groupValues?.get(1)?.toLongOrNull() ?: 0
        }
    }
}
