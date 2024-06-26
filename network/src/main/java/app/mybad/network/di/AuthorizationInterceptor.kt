package app.mybad.network.di

import android.util.Log
import app.mybad.domain.models.AuthToken
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            if (AuthToken.token.isNotBlank()) {
                Log.d("VTTAG", "AuthorizationInterceptor: auth with token ${AuthToken.token}")
                chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "android" // не менять, применяется для смены пароля, при изменении, изменить на беке
                    )
                    .addHeader(
                        "Authorization",
                        "Bearer ${AuthToken.token}"
                    )
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()
            } else chain.request()
        )
    }
}
