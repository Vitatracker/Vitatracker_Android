package app.mybad.notifier.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import app.mybad.data.UserDataModel
import app.mybad.data.UserNotificationsDataModel
import app.mybad.data.UserPersonalDataModel
import app.mybad.data.UserRulesDataModel
import app.mybad.data.UserSettingsDataModel
import app.mybad.data.datastore.serialize.*
import app.mybad.data.repos.*
import app.mybad.data.room.MedDAO
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.repos.UserDataRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton




@Module
@InstallIn(SingletonComponent::class)
class DataModule {



    @Provides
    @Singleton
    fun providesUserRepo(
        dataStore_userData: DataStore<UserDataModel>,
        dataStore_userNotification: DataStore<UserNotificationsDataModel>,
        dataStore_userPersonal: DataStore<UserPersonalDataModel>,
        dataStore_userRules: DataStore<UserRulesDataModel>,
        dataStore_userSettings: DataStore<UserSettingsDataModel>
    ): UserDataRepo {
        return UserDataRepoImpl(
            dataStore_userData,
            dataStore_userNotification,
            dataStore_userPersonal,
            dataStore_userRules,
            dataStore_userSettings
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