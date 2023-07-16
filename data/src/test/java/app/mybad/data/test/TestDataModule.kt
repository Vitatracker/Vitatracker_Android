package app.mybad.data.test

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import app.mybad.data.repos.CourseRepositoryImpl
import app.mybad.data.repos.RemedyRepositoryImpl
import app.mybad.data.repos.UsageRepositoryImpl
import app.mybad.data.repos.UserDataRepoImpl
import app.mybad.data.db.dao.RemedyDao
import app.mybad.data.db.MedDbImpl
import app.mybad.data.db.dao.CourseDao
import app.mybad.data.db.dao.UsageDao
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.network.SettingsNetworkRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.UserDataRepo
import app.mybad.notifier.di.DataModule
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
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
        settingsNetworkRepository: SettingsNetworkRepository
    ): UserDataRepo {
        return UserDataRepoImpl(
            dataStore_userNotification,
            dataStore_userPersonal,
            dataStore_userRules,
            settingsNetworkRepository,
        )
    }

    @Provides
    @Singleton
    fun providesCoursesRepo(
        db: CourseDao,
        @Named("IoDispatcher") dispatcher: CoroutineDispatcher,
    ): CourseRepository {
        return CourseRepositoryImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun providesMedsRepo(
        db: RemedyDao,
        @Named("IoDispatcher") dispatcher: CoroutineDispatcher,
    ): RemedyRepository {
        return RemedyRepositoryImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun providesUsageRepository(
        db: UsageDao,
        @Named("IoDispatcher") dispatcher: CoroutineDispatcher,
    ): UsageRepository {
        return UsageRepositoryImpl(db, dispatcher)
    }
}
