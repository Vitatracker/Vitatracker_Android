package app.mybad.data.repos

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.mybad.data.datastore.DataStorePref
import app.mybad.data.datastore.PreferencesKeys
import kotlinx.coroutines.flow.first
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

    override suspend fun getToken(): String {
        Log.d("DataStore", "getToken: ${PreferencesKeys.token}")
        return dataStore.data.first().toString()
//            .catch { exception ->
//                // dataStore.data throws an IOException when an error is encountered when reading data
//                if (exception is IOException) {
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }.map { preferences ->
//                preferences[PreferencesKeys.token].first() ?: ""
//            }
    }
}
