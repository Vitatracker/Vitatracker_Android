package app.mybad.data.repos

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import app.mybad.data.datastore.DataStorePref
import app.mybad.data.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePrefImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorePref {

    override suspend fun updateToken(token: String) {
        dataStore.edit { it[PreferencesKeys.token] = token }
        Log.d("DataStore", "updateToken: $token")
    }

    override suspend fun getToken(): Flow<String> {
        Log.d("DataStore", "getToken: ${PreferencesKeys.token}")
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
}
