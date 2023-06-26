package app.mybad.domain.repos

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val observeToken: Flow<String>
    val observeRefreshToken: Flow<String>
    val observeUserId: Flow<Long>
    val observeEmail: Flow<String>

    suspend fun updateToken(token: String)
    suspend fun updateRefreshToken(token: String)
    suspend fun updateUserId(userId: Long)
    suspend fun updateEmail(email: String)
}
