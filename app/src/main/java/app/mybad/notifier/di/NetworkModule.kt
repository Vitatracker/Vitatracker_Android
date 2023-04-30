package app.mybad.notifier.di

import app.mybad.network.api.AuthorizationApiRepo
import app.mybad.network.api.CoursesApiRepo
import app.mybad.network.api.SettingsApiRepo
import app.mybad.network.repos.impl.AuthorizationNetworkRepoImpl
import app.mybad.network.repos.impl.CoursesNetworkRepoImpl
import app.mybad.network.repos.impl.SettingsNetworkRepoImpl
import app.mybad.network.repos.repo.AuthorizationNetworkRepo
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.network.repos.repo.SettingsNetworkRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesAuthorizationNetworkRepo(
        authorizationApiRepo: AuthorizationApiRepo
    ): AuthorizationNetworkRepo {
        return AuthorizationNetworkRepoImpl(authorizationApiRepo = authorizationApiRepo)
    }

    @Provides
    @Singleton
    fun providesCoursesNetworkRepo(
        coursesApiRepo: CoursesApiRepo
    ): CoursesNetworkRepo {
        return CoursesNetworkRepoImpl(coursesApiRepo = coursesApiRepo)
    }

    @Provides
    @Singleton
    fun providesSettingsNetworkRepo(
        settingsApiRepo: SettingsApiRepo
    ): SettingsNetworkRepo {
        return SettingsNetworkRepoImpl(settingsApiRepo = settingsApiRepo)
    }

}