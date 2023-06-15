package app.mybad.data.repos

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import app.mybad.data.datastore.PreferencesKeys
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.network.models.AuthToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class DataStoreRepoImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : DataStoreRepo {

    override suspend fun getToken(): Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            AuthToken.token = preferences[PreferencesKeys.token] ?: ""
            AuthToken.token
        }.flowOn(dispatcher)

    override suspend fun updateToken(token: String) {
        withContext(dispatcher) {
            AuthToken.token = token
            dataStore.edit { it[PreferencesKeys.token] = token }
        }
    }

    override suspend fun getUserId(): Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.w("VTTAG", "getUserId error: ${exception.message}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.userId] ?: "0"
        }.flowOn(dispatcher)

    override suspend fun updateUserId(userId: String) {
        withContext(dispatcher) {
            dataStore.edit { it[PreferencesKeys.userId] = userId }
        }
    }
}
