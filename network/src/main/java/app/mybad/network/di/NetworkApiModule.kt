package app.mybad.network.di

import app.mybad.network.api.AuthorizationApi
import app.mybad.network.api.CoursesApi
import app.mybad.network.api.SettingsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    private const val BASE_URL = "http://vitatracker-heroku.herokuapp.com/"

    @Named("LoggingInterceptor")
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Named("AuthInterceptor")
    @Provides
    fun provideAuthorizationInterceptor(): Interceptor = AuthorizationInterceptor()

    @Provides
    fun provideOkHttpClient(
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
        @Named("AuthInterceptor") authInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .addNetworkInterceptor(authInterceptor)
            .retryOnConnectionFailure(false) // not necessary but useful!
            .build()
    }

    @Provides
    fun provideConverter(): Converter.Factory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthorizationApiService(retrofit: Retrofit): AuthorizationApi =
        retrofit.create()

    @Singleton
    @Provides
    @Named("c_api")
    fun provideCoursesApiService(retrofit: Retrofit): CoursesApi =
        retrofit.create()

    @Singleton
    @Provides
    fun provideSettingsApiService(retrofit: Retrofit): SettingsApi =
        retrofit.create()
}
