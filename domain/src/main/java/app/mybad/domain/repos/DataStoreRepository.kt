package app.mybad.domain.repos

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val observeToken: Flow<String>
    val observeUserId: Flow<Long>
    val observeMail: Flow<String>

    suspend fun updateToken(token: String)
    suspend fun updateUserId(userId: Long)
    suspend fun updateMail(mail: String)
}
