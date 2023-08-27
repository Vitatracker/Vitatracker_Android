package app.mybad.notifier.di

import app.mybad.data.repos.CourseRepositoryImpl
import app.mybad.data.repos.PatternUsageRepositoryImpl
import app.mybad.data.repos.RemedyRepositoryImpl
import app.mybad.data.repos.UsageRepositoryImpl
import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.data.repos.UserRepositoryImpl
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.UserDataRepo
import app.mybad.domain.repository.UserRepository
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
    fun provideUserDataRepo(impl: UserDataRepoImpl): UserDataRepo

    @Binds
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun provideCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    @Singleton
    fun provideRemedyRepository(impl: RemedyRepositoryImpl): RemedyRepository

    @Binds
    @Singleton
    fun provideUsageRepository(impl: UsageRepositoryImpl): UsageRepository

    @Binds
    @Singleton
    fun providePatternUsageRepository(impl: PatternUsageRepositoryImpl): PatternUsageRepository

}
