package app.mybad.data.di

import android.content.Context
import androidx.room.Room
import app.mybad.data.db.MedDb
import app.mybad.data.db.MedDbImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providesMedDB(@ApplicationContext app: Context):MedDb = Room
        .databaseBuilder(
            app,
            MedDbImpl::class.java,
            "meds.db"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesDao(db: MedDb) = db.getMedDao()
}
