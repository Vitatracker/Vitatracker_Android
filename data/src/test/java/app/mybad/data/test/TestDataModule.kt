package app.mybad.data.test

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import app.mybad.data.UserNotificationsDataModel
import app.mybad.data.UserPersonalDataModel
import app.mybad.data.UserRulesDataModel
import app.mybad.data.repos.CoursesRepoImpl
import app.mybad.data.repos.MedsRepoImpl
import app.mybad.data.repos.UsagesRepoImpl
import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.data.room.MedDAO
import app.mybad.data.room.MedDB
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.notifier.di.DataModule
import app.mybad.notifier.di.DataStoreModule
import app.mybad.notifier.di.NotificationsModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.jetbrains.annotations.TestOnly
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
class TestDataModule {

    @Provides
    @Named("test_db")
    fun providesInMemoryDb(@ApplicationContext context: Context) : MedDB {
        return Room.inMemoryDatabaseBuilder(
            context,
            MedDB::class.java
        ).allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun providesUserRepo(
        dataStore_userNotification: DataStore<UserNotificationsDataModel>,
        dataStore_userPersonal: DataStore<UserPersonalDataModel>,
        dataStore_userRules: DataStore<UserRulesDataModel>
    ): UserDataRepo {
        return UserDataRepoImpl(
            dataStore_userNotification,
            dataStore_userPersonal,
            dataStore_userRules
        )
    }

    @Provides
    @Singleton
    fun providesCoursesRepo(
        db: MedDAO
    ): CoursesRepo {
        return CoursesRepoImpl(db)
    }

    @Provides
    @Singleton
    fun providesMedsRepo(
        db: MedDAO
    ): MedsRepo {
        return MedsRepoImpl(db)
    }

    @Provides
    @Singleton
    fun providesUsagesRepo(
        db: MedDAO
    ): UsagesRepo {
        return UsagesRepoImpl(db)
    }
}