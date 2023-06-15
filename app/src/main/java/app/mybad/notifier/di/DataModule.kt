package app.mybad.notifier.di

import app.mybad.data.repos.AuthorizationRepoImpl
import app.mybad.data.repos.CoursesRepoImpl
import app.mybad.data.repos.DataStoreRepoImpl
import app.mybad.data.repos.MedsRepoImpl
import app.mybad.data.repos.UsagesRepoImpl
import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.domain.repos.AuthorizationRepo
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun providesUserRepo(impl: UserDataRepoImpl): UserDataRepo

    @Binds
    @Singleton
    fun providesCoursesRepo(impl: CoursesRepoImpl): CoursesRepo

    @Binds
    @Singleton
    fun providesMedsRepo(impl: MedsRepoImpl): MedsRepo

    @Binds
    @Singleton
    fun providesUsagesRepo(impl: UsagesRepoImpl): UsagesRepo

    @Binds
    @Singleton
    fun providesAuthorizationRepo(impl: AuthorizationRepoImpl): AuthorizationRepo

    @Binds
    @Singleton
    fun providesDataStoreRepo(impl: DataStoreRepoImpl): DataStoreRepo
}
