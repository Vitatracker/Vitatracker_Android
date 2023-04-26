package app.mybad.data.datastore

import kotlinx.coroutines.flow.Flow

interface DataStorePref {
    suspend fun updateToken(token: String)
    suspend fun getToken(): Flow<String>
}
