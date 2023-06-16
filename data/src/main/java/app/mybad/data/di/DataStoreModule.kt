package app.mybad.data.di

import app.mybad.data.repos.DataStoreRepositoryImpl
import app.mybad.domain.repos.DataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    @Binds
    @Singleton
    fun provideDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository

}
