package app.mybad.domain.repository.network

import android.content.Intent
import net.openid.appauth.TokenRequest

interface AuthorizationGoogleRepository {
    suspend fun getAuthRequestIntent(): Intent
    suspend fun getEndSessionRequestIntent(): Intent
    suspend fun performTokenRequest(tokenRequest: TokenRequest): Boolean
    suspend fun updateTokenRequest(): Boolean
    suspend fun clearAuthToken()
}
