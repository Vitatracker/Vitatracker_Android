package app.mybad.domain.repos

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {

    suspend fun getToken(): Flow<String>

    suspend fun updateToken(token: String)

    suspend fun getUserId(): Flow<String>

    suspend fun updateUserId(userId: String)
}
