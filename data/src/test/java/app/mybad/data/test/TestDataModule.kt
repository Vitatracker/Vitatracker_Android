package app.mybad.data.test

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import app.mybad.data.repos.CoursesRepoImpl
import app.mybad.data.repos.MedsRepoImpl
import app.mybad.data.repos.UsagesRepoImpl
import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.data.db.dao.MedDao
import app.mybad.data.db.MedDbImpl
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.notifier.di.DataModule
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
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
    fun providesInMemoryDb(@ApplicationContext context: Context): MedDbImpl {
        return Room.inMemoryDatabaseBuilder(
            context,
            MedDbImpl::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun providesUserRepo(
        dataStore_userNotification: DataStore<UserNotificationsDataModel>,
        dataStore_userPersonal: DataStore<UserPersonalDataModel>,
        dataStore_userRules: DataStore<UserRulesDataModel>,
        settingsNetworkRepo: SettingsNetworkRepo
    ): UserDataRepo {
        return UserDataRepoImpl(
            dataStore_userNotification,
            dataStore_userPersonal,
            dataStore_userRules,
            settingsNetworkRepo
        )
    }

    @Provides
    @Singleton
    fun providesCoursesRepo(
        db: MedDao
    ): CoursesRepo {
        return CoursesRepoImpl(db)
    }

    @Provides
    @Singleton
    fun providesMedsRepo(
        db: MedDao
    ): MedsRepo {
        return MedsRepoImpl(db)
    }

    @Provides
    @Singleton
    fun providesUsagesRepo(
        db: MedDao
    ): UsagesRepo {
        return UsagesRepoImpl(db)
    }
}
