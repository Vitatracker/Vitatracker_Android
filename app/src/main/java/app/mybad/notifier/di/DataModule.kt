package app.mybad.notifier.di

import app.mybad.data.repos.*
import app.mybad.data.room.MedDAO
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun providesUserDataRepo(): UserDataRepo {
        return UserDataRepoImpl()
    }

    @Provides
    @Singleton
    fun providesCoursesRepo(
        db: MedDAO
    ) : CoursesRepo {
        return CoursesRepoImpl(db)
    }

    @Provides
    @Singleton
    fun providesMedsRepo(
        db: MedDAO
    ) : MedsRepo {
        return MedsRepoImpl(db)
    }

    @Provides
    @Singleton
    fun providesUsagesRepo(
        db: MedDAO
    ) : UsagesRepo {
        return UsagesRepoImpl(db)
    }
}