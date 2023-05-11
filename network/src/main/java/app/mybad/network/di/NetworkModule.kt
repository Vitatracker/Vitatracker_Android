package app.mybad.network.di

import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.network.api.AuthorizationApiRepo
import app.mybad.network.api.CoursesApi
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
import javax.inject.Inject
import javax.inject.Named
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
        @Named("c_api") coursesApi: CoursesApi,
        dataStoreRepo: DataStoreRepo,
        coursesRepo: CoursesRepo,
        medsRepo: MedsRepo,
        usagesRepo: UsagesRepo
    ): CoursesNetworkRepo {
        return CoursesNetworkRepoImpl(
            coursesApi = coursesApi,
            dataStoreRepo = dataStoreRepo,
            coursesRepo = coursesRepo,
            usagesRepo = usagesRepo,
            medsRepo = medsRepo,
        )
    }

    @Provides
    @Singleton
    fun providesSettingsNetworkRepo(
        settingsApiRepo: SettingsApiRepo
    ): SettingsNetworkRepo {
        return SettingsNetworkRepoImpl(settingsApiRepo = settingsApiRepo)
    }

}