package app.mybad.data.modules

import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.domain.repos.UserDataRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDataModule {

    @Provides
    @Singleton
    fun providesUserDataRepo(impl: UserDataRepoImpl): UserDataRepo {
        return UserDataRepoImpl()
    }
}