package app.mybad.notifier.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import app.mybad.data.datastore.serialize.UserNotificationDataModelSerializer
import app.mybad.data.datastore.serialize.UserPersonalDataModelSerializer
import app.mybad.data.datastore.serialize.UserRulesDataModelSerializer
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val NOTIFICATION_STORE_FILE_NAME = "user_notification.pb"
private const val PERSONAL_STORE_FILE_NAME = "user_personal.pb"
private const val RULES_STORE_FILE_NAME = "user_rules.pb"

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Singleton
    @Provides
    fun provideProtoDataStoreUserNotification(
        @ApplicationContext appContext: Context
    ): DataStore<UserNotificationsDataModel> {
        return DataStoreFactory.create(
            serializer = UserNotificationDataModelSerializer,
            produceFile = { appContext.dataStoreFile(NOTIFICATION_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserNotificationsDataModel.getDefaultInstance() }
            ),
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStoreUserPersonal(
        @ApplicationContext appContext: Context
    ): DataStore<UserPersonalDataModel> {
        return DataStoreFactory.create(
            serializer = UserPersonalDataModelSerializer,
            produceFile = { appContext.dataStoreFile(PERSONAL_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPersonalDataModel.getDefaultInstance() }
            ),
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStoreUserRules(
        @ApplicationContext appContext: Context
    ): DataStore<UserRulesDataModel> {
        return DataStoreFactory.create(
            serializer = UserRulesDataModelSerializer,
            produceFile = { appContext.dataStoreFile(RULES_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserRulesDataModel.getDefaultInstance() }
            ),
        )
    }
}
