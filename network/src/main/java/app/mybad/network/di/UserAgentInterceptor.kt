package app.mybad.network.di

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .removeHeader("user-agent")
                .addHeader("user-agent", "android")
                .build()
        )
    }
}
