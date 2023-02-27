package app.mybad.data.modules

import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.domain.repos.UserDataRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserDataModule {

    @Binds
    abstract fun bindSettingsUseCase(settingsUseCaseImpl: UserDataRepoImpl): UserDataRepo

}