package app.mybad.data.repos

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.mybad.data.datastore.DataStorePref
import app.mybad.data.datastore.PreferencesKeys
import kotlinx.coroutines.flow.firstOrNull
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
        return dataStore.data.firstOrNull()?.get(PreferencesKeys.token).toString()
    }
}