package app.mybad.data.modules

import app.mybad.data.repos.CoursesRepoImpl
import app.mybad.data.repos.MedsRepoImpl
import app.mybad.data.repos.UsagesRepoImpl
import app.mybad.data.repos.UserDataRepoImpl
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
    fun providesUserDataRepo(impl: UserDataRepoImpl): UserDataRepo {
        return UserDataRepoImpl()
    }

    @Provides
    @Singleton
    fun providesCoursesRepo(impl: CoursesRepoImpl) : CoursesRepo {
        return CoursesRepoImpl()
    }

    @Provides
    @Singleton
    fun providesMedsRepo(impl: MedsRepoImpl) : MedsRepo {
        return MedsRepoImpl()
    }

    @Provides
    @Singleton
    fun providesUsagesRepo(impl: UsagesRepoImpl) : UsagesRepo {
        return UsagesRepoImpl()
    }
}