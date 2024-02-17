package app.mybad.network.di

import app.mybad.domain.repository.network.AuthorizationFirebaseRepository
import app.mybad.domain.repository.network.AuthorizationGoogleRepository
import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.domain.repository.network.SettingsNetworkRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import app.mybad.network.repository.AuthorizationFirebaseRepositoryImpl
import app.mybad.network.repository.AuthorizationGoogleRepositoryImpl
import app.mybad.network.repository.AuthorizationNetworkRepositoryImpl
import app.mybad.network.repository.CourseNetworkRepositoryImpl
import app.mybad.network.repository.RemedyNetworkRepositoryImpl
import app.mybad.network.repository.SettingsNetworkRepoImpl
import app.mybad.network.repository.UsageNetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun provideAuthorizationNetworkRepository(impl: AuthorizationNetworkRepositoryImpl): AuthorizationNetworkRepository

    @Binds
    @Singleton
    fun provideAuthorizationGoogleRepository(impl: AuthorizationGoogleRepositoryImpl): AuthorizationGoogleRepository

    @Binds
    @Singleton
    fun provideAuthorizationFirebaseRepository(impl: AuthorizationFirebaseRepositoryImpl): AuthorizationFirebaseRepository

    @Binds
    @Singleton
    fun provideRemedyNetworkRepository(impl: RemedyNetworkRepositoryImpl): RemedyNetworkRepository

    @Binds
    @Singleton
    fun provideCourseNetworkRepository(impl: CourseNetworkRepositoryImpl): CourseNetworkRepository

    @Binds
    @Singleton
    fun provideUsageNetworkRepository(impl: UsageNetworkRepositoryImpl): UsageNetworkRepository

    @Binds
    @Singleton
    fun provideSettingsNetworkRepository(impl: SettingsNetworkRepoImpl): SettingsNetworkRepository
}
