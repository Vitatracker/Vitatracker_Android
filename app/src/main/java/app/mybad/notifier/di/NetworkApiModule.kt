package app.mybad.notifier.di

import app.mybad.network.api.AuthorizationApiRepo
import app.mybad.network.BuildConfig
import app.mybad.network.api.CoursesApiRepo
import app.mybad.network.api.SettingsApiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    private const val BASE_URL = "http://vitatracker-001-site1.atempurl.com/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            if(BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        }
        val client = OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthorizationApiService(retrofit: Retrofit): AuthorizationApiRepo =
        retrofit.create(AuthorizationApiRepo::class.java)

    @Singleton
    @Provides
    fun provideCoursesApiService(retrofit: Retrofit): CoursesApiRepo =
        retrofit.create(CoursesApiRepo::class.java)

    @Singleton
    @Provides
    fun provideSettingsApiService(retrofit: Retrofit): SettingsApiRepo =
        retrofit.create(SettingsApiRepo::class.java)

}