package app.mybad.network.di

import android.util.Log
import app.mybad.network.models.AuthToken
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = AuthToken.token
        return chain.proceed(
            if (token.isNotBlank()) {
                Log.w("VTTAG", "auth with token $token")
                chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer $token"
                    )
                    .build()
            } else chain.request()
        )
    }
}
