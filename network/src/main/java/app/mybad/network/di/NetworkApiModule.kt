package app.mybad.network.di

import android.util.Log
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.network.api.AuthorizationApiRepo
import app.mybad.network.BuildConfig
import app.mybad.network.api.CoursesApi
import app.mybad.network.api.SettingsApiRepo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    private const val BASE_URL = "http://vitatracker-heroku.herokuapp.com/"

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        dataStoreRepo: DataStoreRepo
    ): Retrofit {
        val scope = CoroutineScope(Dispatchers.IO)
        val interceptor = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        }
        var token = ""
        scope.launch {
            dataStoreRepo.getToken().collect {
                token = it
            }
        }
        val authInterceptor = Interceptor {
            var r = it.request()
            if (token.isNotBlank()) {
                r = r.newBuilder().addHeader("Authorization", "Bearer $token").build()
                Log.w("NAM", "auth with token $token")
            }
            it.proceed(r)
        }
        val client = OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
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
    @Named("c_api")
    fun provideCoursesApiService(retrofit: Retrofit): CoursesApi =
        retrofit.create(CoursesApi::class.java)

    @Singleton
    @Provides
    fun provideSettingsApiService(retrofit: Retrofit): SettingsApiRepo =
        retrofit.create(SettingsApiRepo::class.java)
}
