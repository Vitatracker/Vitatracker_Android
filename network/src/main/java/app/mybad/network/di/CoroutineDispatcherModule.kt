package app.mybad.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class CoroutineDispatcherModule {

    @Named("IoDispatcher")
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
