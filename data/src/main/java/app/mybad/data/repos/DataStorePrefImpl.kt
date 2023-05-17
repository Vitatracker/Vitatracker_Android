package app.mybad.data.repos

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import app.mybad.data.datastore.DataStorePref
import app.mybad.data.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePrefImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorePref {

    override suspend fun updateToken(token: String) {
        dataStore.edit { it[PreferencesKeys.token] = token }
    }

    override suspend fun getToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[PreferencesKeys.token] ?: ""
            }
    }

    override suspend fun updateUserId(userId: String) {
        dataStore.edit { it[PreferencesKeys.userId] = userId }
    }

    override suspend fun getUserId(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[PreferencesKeys.userId] ?: ""
            }
    }
}
