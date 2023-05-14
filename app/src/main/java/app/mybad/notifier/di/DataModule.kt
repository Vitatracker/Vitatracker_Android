package app.mybad.notifier.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.mybad.data.datastore.serialize.*
import app.mybad.data.repos.*
import app.mybad.data.room.MedDAO
import app.mybad.domain.repos.AuthorizationRepo
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.network.repos.repo.AuthorizationNetworkRepo
import app.mybad.network.repos.repo.SettingsNetworkRepo
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesUserRepo(
        dataStore_userNotification: DataStore<UserNotificationsDataModel>,
        dataStore_userPersonal: DataStore<UserPersonalDataModel>,
        dataStore_userRules: DataStore<UserRulesDataModel>,
        settingsNetworkRepo: SettingsNetworkRepo
    ): UserDataRepo {
        return UserDataRepoImpl(
            dataStore_userNotification = dataStore_userNotification,
            dataStore_userPersonal = dataStore_userPersonal,
            dataStore_userRules = dataStore_userRules,
            settingsNetworkRepo = settingsNetworkRepo
        )
    }

    @Provides
    @Singleton
    fun providesCoursesRepo(
        db: MedDAO,
    ): CoursesRepo {
        return CoursesRepoImpl(db = db)
    }

    @Provides
    @Singleton
    fun providesMedsRepo(
        db: MedDAO
    ): MedsRepo {
        return MedsRepoImpl(db = db)
    }

    @Provides
    @Singleton
    fun providesUsagesRepo(
        db: MedDAO
    ): UsagesRepo {
        return UsagesRepoImpl(db = db)
    }

    @Provides
    @Singleton
    fun providesAuthorizationRepo(
        dataStore: DataStore<Preferences>,
        authorizationNetworkRepo: AuthorizationNetworkRepo
    ): AuthorizationRepo {
        return AuthorizationRepoImpl(
            dataStore = dataStore,
            authorizationNetworkRepo = authorizationNetworkRepo
        )
    }

    @Provides
    @Singleton
    fun providesDataStoreRepo(
        dataStore: DataStore<Preferences>
    ): DataStoreRepo {
        return DataStoreRepoImpl(dataStore = dataStore)
    }
}
