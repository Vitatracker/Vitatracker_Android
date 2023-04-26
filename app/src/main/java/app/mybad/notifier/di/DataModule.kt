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

    @Provides
    @Singleton
    fun providesAuthorizationRepo(
        dataStore: DataStore<Preferences>
    ): AuthorizationRepo {
        return AuthorizationRepoImpl(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun providesDataStoreRepo(
        dataStore: DataStore<Preferences>
    ): DataStoreRepo {
        return DataStoreRepoImpl(dataStore = dataStore)
    }
}
