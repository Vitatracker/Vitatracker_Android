package app.mybad.network.repository

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import app.mybad.domain.models.AuthConfig
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.network.AuthorizationGoogleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthorizationGoogleRepositoryImpl @Inject constructor(
    private val authService: AuthorizationService,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : AuthorizationGoogleRepository {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTHORIZE_URI), // конечной точки авторизации
        Uri.parse(AuthConfig.TOKEN_URI), // конечная точка токена
        null, // registration endpoint
        Uri.parse(AuthConfig.LOGOUT_URI) // logout
    )

    override suspend fun getAuthRequestIntent(): Intent = withContext(dispatcher) {
        authService.getAuthorizationRequestIntent(getAuthRequest())
    }

    private suspend fun getAuthRequest(): AuthorizationRequest = withContext(dispatcher) {
        AuthorizationRequest.Builder(
            serviceConfiguration, // конфигурации службы авторизации
            AuthConfig.CLIENT_ID, // ИД клиента, правило предварительно зарегистрировано и статическое
            AuthConfig.RESPONSE_TYPE, // значение response_type: нам нужен код
            AuthConfig.CALLBACK_URL.toUri() // URI перенаправления, на который отправляется ответ аутентификации
        )
            .setAdditionalParameters(mapOf("approval_prompt" to AuthConfig.APPROVAL_PROMPT))
            .setScope(AuthConfig.SCOPE)
//            .setCodeVerifier(null) // решает некоторые проблемы с авторизацией
            .build()
            .also {
                Log.d(
                    "VTTAG",
                    "AuthorizationGoogleRepositoryImpl::getAuthRequest request=${it.toUri()}"
                )
            }
    }

    override suspend fun getEndSessionRequestIntent(): Intent = withContext(dispatcher) {
        authService.getEndSessionRequestIntent(getEndSessionRequest())
    }

    private fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(AuthConfig.CALLBACK_URL.toUri())
            .build()
    }

    override suspend fun performTokenRequest(tokenRequest: TokenRequest): Boolean {
        return withContext(dispatcher) {
            Log.d("VTTAG", "AuthorizationGoogleRepositoryImpl::performTokenRequest")
            // выполнить запрос токена
            suspendCoroutine { continuation ->
                authService.performTokenRequest(
                    tokenRequest,
                    getClientAuthentication(),
                ) { response, exception ->
                    Log.d("VTTAG", "AuthorizationGoogleRepositoryImpl::performTokenRequest request")
                    AuthToken.clear()
                    response?.run {
                        Log.d(
                            "VTTAG",
                            "AuthorizationGoogleRepositoryImpl::performTokenRequest accessToken=$accessToken"
                        )
                        Log.d(
                            "VTTAG",
                            "AuthorizationGoogleRepositoryImpl::performTokenRequest refreshToken=$refreshToken"
                        )
                        Log.d(
                            "VTTAG",
                            "AuthorizationGoogleRepositoryImpl::performTokenRequest accessTokenExpirationTime=$accessTokenExpirationTime"
                        )
                        // сохраним токен
                        AuthToken.token = accessToken.orEmpty()
                        AuthToken.tokenRefresh = refreshToken.orEmpty()
//                        AuthToken.tokenExpirationTime = accessTokenExpirationTime ?: 0
//                        AuthToken.tokenType = tokenType.orEmpty()
                    }
                    // отправим подтверждение получения токена
                    continuation.resume(AuthToken.token.isNotBlank())
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    // обновим токен
    override suspend fun updateTokenRequest(): Boolean = withContext(dispatcher) {
        val currentTime = System.currentTimeMillis()
        Log.w(
            "VTTAG", "PersonRepositoryImp|updateToken now=${currentTime} exp=${
                AuthToken.tokenDate
            } time=${AuthToken.tokenDate - currentTime}"
        )
        // если время прошло, то выполнить запрос токена
        if (AuthToken.tokenDate != 0L &&
            AuthToken.tokenDate - currentTime <= 0
        ) performTokenRequest(getRefreshTokenRequest())
        else true
    }

    private fun getRefreshTokenRequest(): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setRefreshToken(AuthToken.tokenRefresh)
            .build()
    }

    // очистим токен
    override suspend fun clearAuthToken() {
        AuthToken.clear()
    }
}
