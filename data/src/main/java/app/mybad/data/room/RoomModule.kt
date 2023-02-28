package app.mybad.data.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun providesMedDB(@ApplicationContext app: Context) = Room
        .databaseBuilder(
            app,
            MedDB::class.java,
            "meds.db"
        ).build()

    @Provides
    @Singleton
    fun providesDao(db: MedDB) = db.dao()
}