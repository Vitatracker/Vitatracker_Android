package app.mybad.network.di

import android.util.Log
import app.mybad.domain.models.AuthToken
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            if (AuthToken.token.isNotBlank()) {
                Log.w("VTTAG", "auth with token ${AuthToken.token}")
                chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${AuthToken.token}"
                    )
                    .build()
            } else chain.request()
        )
    }
}
