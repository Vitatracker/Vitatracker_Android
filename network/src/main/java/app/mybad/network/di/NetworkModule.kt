package app.mybad.network.di

import app.mybad.network.repos.impl.AuthorizationNetworkRepoImpl
import app.mybad.network.repos.impl.CoursesNetworkRepoImpl
import app.mybad.network.repos.impl.SettingsNetworkRepoImpl
import app.mybad.network.repos.repo.AuthorizationNetworkRepo
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.network.repos.repo.SettingsNetworkRepo
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
    fun providesAuthorizationNetworkRepo(impl: AuthorizationNetworkRepoImpl): AuthorizationNetworkRepo

    @Binds
    @Singleton
    fun providesCoursesNetworkRepo(impl: CoursesNetworkRepoImpl): CoursesNetworkRepo

    @Binds
    @Singleton
    fun providesSettingsNetworkRepo(impl: SettingsNetworkRepoImpl): SettingsNetworkRepo
}
