package app.mybad.notifier.di

import app.mybad.data.repos.AuthorizationRepoImpl
import app.mybad.data.repos.CoursesRepoImpl
import app.mybad.data.repos.MedsRepoImpl
import app.mybad.data.repos.UsagesRepoImpl
import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.data.repos.UsersRepositoryImpl
import app.mybad.domain.repos.AuthorizationRepo
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.repos.UsersRepository
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
    fun provideUserRepo(impl: UserDataRepoImpl): UserDataRepo

    @Binds
    @Singleton
    fun provideCoursesRepo(impl: CoursesRepoImpl): CoursesRepo

    @Binds
    @Singleton
    fun provideMedsRepo(impl: MedsRepoImpl): MedsRepo

    @Binds
    @Singleton
    fun provideUsagesRepo(impl: UsagesRepoImpl): UsagesRepo

    @Binds
    @Singleton
    fun provideAuthorizationRepo(impl: AuthorizationRepoImpl): AuthorizationRepo

    @Binds
    @Singleton
    fun provideUsersRepository(impl: UsersRepositoryImpl): UsersRepository

}
