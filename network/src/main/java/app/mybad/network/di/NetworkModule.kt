package app.mybad.network.di

import app.mybad.domain.repos.AuthorizationNetworkRepository
import app.mybad.domain.repos.CoursesNetworkRepo
import app.mybad.domain.repos.SettingsNetworkRepository
import app.mybad.network.repos.impl.AuthorizationNetworkRepoImpl
import app.mybad.network.repos.impl.CoursesNetworkRepoImpl
import app.mybad.network.repos.impl.SettingsNetworkRepoImpl
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
    fun providesAuthorizationNetworkRepo(impl: AuthorizationNetworkRepoImpl): AuthorizationNetworkRepository

    @Binds
    @Singleton
    fun providesCoursesNetworkRepo(impl: CoursesNetworkRepoImpl): CoursesNetworkRepo

    @Binds
    @Singleton
    fun providesSettingsNetworkRepo(impl: SettingsNetworkRepoImpl): SettingsNetworkRepository
}
