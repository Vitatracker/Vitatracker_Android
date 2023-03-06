package app.mybad.data.repos

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.mybad.data.datastore.DataStorePref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class DataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorePref {

    private object Keys {
        val token = stringPreferencesKey("token")
    }

    override suspend fun updateToken(token: String) {
        dataStore.edit { it[Keys.token] = token }
        Log.d("DataStore", "updateToken: $token")
    }

    override suspend fun getToken() {
        Log.d("DataStore", "getToken: ${Keys.token}")
        return dataStore.data.collectLatest { Keys.token }
    }

}