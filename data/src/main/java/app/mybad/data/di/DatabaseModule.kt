package app.mybad.data.di

import android.content.Context
import androidx.room.Room
import app.mybad.data.db.MedDb
import app.mybad.data.db.MedDbImpl
import app.mybad.data.db.dao.CourseDao
import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.db.dao.UsageDao
import app.mybad.data.db.dao.UserDao
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
    fun provideMedDB(@ApplicationContext app: Context): MedDb = Room
        .databaseBuilder(
            app,
            MedDbImpl::class.java,
            MedDbImpl.DB_NAME
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideUsersDao(db: MedDb): UserDao = db.getUserDao()

    @Provides
    @Singleton
    fun provideMedDao(db: MedDb): RemedyDao = db.getRemedyDao()

    @Provides
    @Singleton
    fun provideCourseDao(db: MedDb): CourseDao = db.getCourseDao()

    @Provides
    @Singleton
    fun provideUsageDao(db: MedDb): UsageDao = db.getUsageDao()
}
