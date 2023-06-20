package app.mybad.data.repos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.mybad.domain.repos.DataStoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : DataStoreRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    override val observeToken= context.dataStore.data.map { it[TOKEN] ?: "" }
            .flowOn(dispatcher)
    override val observeUserId = context.dataStore.data.map { it[USERID] ?: -1L }
            .flowOn(dispatcher)
    override val observeEmail = context.dataStore.data.map { it[EMAIL] ?: "" }
            .flowOn(dispatcher)

    override suspend fun updateToken(token: String) {
        withContext(dispatcher) {
            context.dataStore.edit { it[TOKEN] = token }
        }
    }

    override suspend fun updateUserId(userId: Long) {
        withContext(dispatcher) {
            context.dataStore.edit { it[USERID] = userId }
        }
    }

    override suspend fun updateEmail(email: String) {
        withContext(dispatcher) {
            context.dataStore.edit { it[EMAIL] = email }
        }
    }

    private companion object {
        const val DATASTORE_NAME = "user_preferences"
        val TOKEN = stringPreferencesKey("token")
        val USERID = longPreferencesKey("userId")
        val EMAIL = stringPreferencesKey("email")
    }

}
