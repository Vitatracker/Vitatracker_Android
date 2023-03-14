package app.mybad.notifier.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import app.mybad.data.*
import app.mybad.data.datastore.serialize.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_USER_FILE_NAME = "user_data.pb"
private const val NOTIFICATION_STORE_FILE_NAME = "user_data.pb"
private const val PERSONAL_STORE_FILE_NAME = "user_data.pb"
private const val RULES_STORE_FILE_NAME = "user_data.pb"
private const val SETTINGS_STORE_FILE_NAME = "user_data.pb"

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore_userData(@ApplicationContext appContext: Context): DataStore<UserDataModel> {
        return DataStoreFactory.create(
            serializer = UserDataModelSerializer,
            produceFile = { appContext.dataStoreFile(DATA_USER_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserDataModel.getDefaultInstance() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore_userNotification(@ApplicationContext appContext: Context): DataStore<UserNotificationsDataModel> {
        return DataStoreFactory.create(
            serializer = UserNotificationDataModelSerializer,
            produceFile = { appContext.dataStoreFile(NOTIFICATION_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserNotificationsDataModel.getDefaultInstance() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore_userPersonal(@ApplicationContext appContext: Context): DataStore<UserPersonalDataModel> {
        return DataStoreFactory.create(
            serializer = UserPersonalDataModelSerializer,
            produceFile = { appContext.dataStoreFile(PERSONAL_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPersonalDataModel.getDefaultInstance() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore_userRules(@ApplicationContext appContext: Context): DataStore<UserRulesDataModel> {
        return DataStoreFactory.create(
            serializer = UserRulesDataModelSerializer,
            produceFile = { appContext.dataStoreFile(RULES_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserRulesDataModel.getDefaultInstance() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore_userSettings(@ApplicationContext appContext: Context): DataStore<UserSettingsDataModel> {
        return DataStoreFactory.create(
            serializer = UserSettingsDataModelSerializer,
            produceFile = { appContext.dataStoreFile(SETTINGS_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserSettingsDataModel.getDefaultInstance() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

}
