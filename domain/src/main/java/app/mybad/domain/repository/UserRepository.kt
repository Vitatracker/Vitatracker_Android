package app.mybad.domain.repository

import app.mybad.domain.models.user.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun isDarkTheme(userId: Long): Flow<Boolean>
    suspend fun getNumberOfUsers(): Long
    suspend fun getUserIdByEmail(email: String): Long?
    suspend fun getUserByEmail(email: String): UserDomainModel
    suspend fun getUserById(userId: Long): UserDomainModel
    suspend fun getUserLastEntrance(): UserDomainModel

    suspend fun insertUser(name: String, email: String): Long
    suspend fun updateMail(userId: Long, email: String)
    suspend fun updateName(userId: Long, name: String)

    suspend fun clearTokenByUserId(userId: Long)
    suspend fun updateTokenByUserId(
        userId: Long,
        token: String,
        tokenDate: Long,
        tokenRefresh: String,
        tokenRefreshDate: Long,
    ): UserDomainModel

    suspend fun markDeletionUserById(userId: Long)
    suspend fun deleteUserById(userId: Long)
    suspend fun updateDateSynchronize(userId: Long)
}
